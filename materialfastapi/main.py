"""Main module for the FastAPI service."""

from fastapi import FastAPI
from fastapi.responses import JSONResponse

from core.scraping import Scraping
from core.material import MaterialList

app = FastAPI(title="Agile Inventory API")
scraper = Scraping("user", "password")


@app.on_event("startup")
async def startup_event():
    """Executa quando o FastAPI inicia."""
    scraper.fetch_csrf_token()
    scraper.login()
    print("Scraper logado!")


@app.get("/")
async def root():
    """Root endpoint for the FastAPI service."""
    return {"message": "FastAPI service started"}


@app.get("/scraping/{order_id}")
async def get_material_data(order_id: str):
    """Endpoint to fetch material data based on a production order ID."""
    MaterialList.clear_instances()
    data = await scraper.get_materials_of_production_order(order_id)
    if not data:
        return {"error": "Failed to fetch material data"}
    return JSONResponse(
        content=data, status_code=200, headers={"Content-Type": "application/json"}
    )