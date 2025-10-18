"""Module for web scraping and login to CargaMaquina."""

from typing import Any
from io import StringIO
import requests
from bs4 import BeautifulSoup
from requests.exceptions import RequestException
import pandas as pd

from core.material import MaterialList


class Scraping(requests.Session):
    """A class to handle web scraping and login to CargaMaquina.
    Args:
        username (str): The username for login.
        password (str): The password for login.
    """

    def __init__(self, username: str, password: str) -> None:
        super().__init__()
        self.username = username
        self.password = password
        self.yii_csrf_token = ""
        self.session = requests.Session()
        self.login_url = "https://v2.cargamaquina.com.br/site/login/c/3.1~13,3%5e17,7"

    def fetch_csrf_token(self) -> bool:
        """Fetch CSRF token from the login page."""
        try:
            response = self.get(self.login_url)
            if not response.ok:
                print(f"Failed to load login page: {response.status_code}")
                return False
            soup = BeautifulSoup(response.text, "html.parser")
            csrf_input = soup.find("input", {"name": "YII_CSRF_TOKEN"})
            if csrf_input:
                self.yii_csrf_token = csrf_input["value"]

            print(f"CSRF token: {self.yii_csrf_token}")
            return True
        except RequestException as e:
            print(f"Error fetching CSRF token: {e}")
            return False

    def login(self) -> bool:
        """Perform login with the provided username and password."""
        try:
            payload = {
                "YII_CSRF_TOKEN": self.yii_csrf_token,
                "LoginForm[username]": self.username,
                "LoginForm[password]": self.password,
                "LoginForm[codigoConexao]": " 3.1~13,3^17,7",
                "yt0": "Entrar",
            }

            response = self.post(self.login_url, data=payload)
            if not response.ok:
                print(f"Login failed: {response.status_code}")
                return False
            print("Login successful")
            return True
        except RequestException as e:
            print(f"Error logging in: {e}")
            return False

    async def get_materials_of_production_order(self, order_id: str) -> dict[str, Any]:
        """Fetch materials of a specific production order.
        Args:
            order_id (int): The ID of the production order.
        """

        def _parse_quantity(value: str) -> float:
            if "," in value and "." in value:
                value = value.replace(".", "").replace(",", ".")
            elif "," in value:
                value = value.replace(",", ".")
            elif value.count(".") > 1:
                value = value.replace(".", "")
            try:
                return float(value)
            except ValueError:
                print(f"[WARN] Valor inválido para conversão: '{value}'")
                return 0.0

        url: str = "https://v2.cargamaquina.com.br/ordemProducao/visualizar/" + order_id
        try:
            response = self.get(url)
            if not response.ok:
                print(f"Failed to load production order page: {response.status_code}")
                return {"error": "Failed to fetch production order page"}
            print("Production order page loaded successfully")

            if response.status_code == 404:
                return {"error": "Production order not found"}
            
            # Clear previous instances
            MaterialList.clear_instances()

            soup = BeautifulSoup(response.text, "html.parser")
            # Get production order number
            element = soup.find("input", {"id": "txtCodigoPrincipal"})
            order_number = str(element["value"]) if element else None  # type: ignore
            if not order_number:
                return {"error": "Failed to fetch production order number"}
            
            # Get production material code
            product_parts = soup.find("input", {"id": "material"})
            product_parts = str(product_parts["value"]) if product_parts else ""  # type: ignore
            if not product_parts:
                return {"error": "Failed to fetch production material"}
            product_code = product_parts.split("-", 1)[0].strip()  
            product_description = product_parts.split("-", 1)[1].strip()

            # Getting all materials from production order
            all_tables = pd.read_html(StringIO(response.text))
            os_df, mp_df = all_tables[0], all_tables[1]
            services: list[str] = ["EMBALAGEM/EXPEDICAO"]
            os_codes = os_df.loc[os_df["Serviço"].isin(services), "Código"].tolist()
            product_quantity = os_df.loc[os_df["Serviço"].isin(services), "Qtde. Prevista"].tolist()
            product_quantity = int(float(product_quantity[-1].split(" ")[0].replace(",", ".")))
            print(f"OS Codes: {os_codes}, Product Quantity: {product_quantity}")

            string_inputs: list[str] = [
                f"Insumos para a OS: {code}" for code in os_codes
            ]

            # Remove strings that are not in mp_df strings
            for string_input in string_inputs:
                found = any(row["Código"].startswith(string_input) for _, row in mp_df.iterrows())
                if not found:
                    string_inputs.remove(string_input)
                if found:
                    print(f"Found section for: {string_input}")


            MaterialList.instances[order_number] = {
                "orderId": int(order_id),
                "orderNumber": order_number,
                "code": product_code,
                "description": product_description,
                "quantity": product_quantity,
                "materials": []
            }

            for string_input in string_inputs:
                start_index = mp_df.index[
                    mp_df["Código"].str.startswith(string_input)
                ].tolist()[0]
                rows = mp_df.iloc[start_index + 1:]
                for i in range(len(rows)):
                    row = rows.iloc[i]

                    if row["Código"].startswith("Insumos"):
                        break

                    material_code: str = row["Código"]
                    material_description: str = row["Nome"]


                    code_exclusion_list: list[str] = [
                        "ANH0",
                        "SILNTR",
                        "ETQBP",
                        "SCPL",
                        "TBRET",
                        "TMOES",
                        "TMF",
                        "TMM",
                        "TME",
                        "TMIL",
                        "TMCP",
                        "TMQ",
                    ]

                    description_exclusion_list: list[str] = [
                        "PAPELÃO",
                        "FITA",
                        "RETRATIL",
                        "ETIQUETA",
                    ]
                    unit_exclusion_list: list[str] = [
                        "mt",
                        "m",
                        "mts",
                        "metro",
                        "metros",
                        "rl",
                        "kg",
                    ]

                    if any(
                        material_code.upper().startswith(excl)
                        for excl in code_exclusion_list
                    ):
                        continue

                    if any(
                        excl in material_description.upper()
                        for excl in description_exclusion_list
                    ):
                        continue
                    parts = row["Quantidade"].split(" ")
                    material_quantity = _parse_quantity(parts[0])
                    material_unit = parts[1].lower() if len(parts) > 1 else ""
                    if material_unit in unit_exclusion_list:
                        continue

                    # Factory method to create Material instances
                    MaterialList.instances[order_number]["materials"].append(
                        {"code": material_code, "description": material_description, "quantity": material_quantity}
                    )

            return MaterialList.instances[order_number]

        except RequestException as e:
            print(f"Error fetching production order page: {e}")
            return {"error": "Failed to fetch production order page"}
        

if __name__ == "__main__":
    scraper = Scraping("user", "password")
    if scraper.fetch_csrf_token() and scraper.login():
        import asyncio
        data = asyncio.run(scraper.get_materials_of_production_order("38034135"))
        print(data)