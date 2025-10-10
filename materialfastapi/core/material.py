"""Module for handling material data."""

import json
from typing import Any
from dataclasses import dataclass, asdict


@dataclass
class Material:
    """A class to represent a material item.

    Args:
        code (str): The code of the material.
        description (str): The description of the material.
        quantity (float): The quantity of the material.

    """

    code: str
    description: str
    quantity: float

    def __init__(self, code: str, description: str, quantity: float) -> None:
        self.code = code
        self.description = description
        self.quantity = quantity


class MaterialList:
    """A class to handle material data."""

    instances: dict[str, Any] = {}

    @classmethod
    def create(
        cls,
        order_id: int,
        order_number: str,
        product_code: str,
        product_description: str,
        product_quantity: int,
        material_code: str,
        material_description: str,
        material_quantity: float,
    ) -> None:
        """Creates an instance of the class."""

        instance = Material(material_code, material_description, material_quantity)

        if order_number not in cls.instances:
            cls.instances[order_number] = {"materials": []}

        cls.instances[order_number]["orderId"] = order_id
        cls.instances[order_number]["orderNumber"] = order_number
        cls.instances[order_number]["code"] = product_code
        cls.instances[order_number]["description"] = product_description
        cls.instances[order_number]["quantity"] = product_quantity
        cls.instances[order_number]["materials"].append(asdict(instance))

    @classmethod
    def get_instances(cls) -> dict[str, Any]:
        """Returns the instances of the class."""
        return cls.instances
    
    @classmethod
    def clear_instances(cls) -> None:
        """Clears the instances of the class."""
        cls.instances = {}

    @classmethod
    def to_json_str(cls) -> str:
        """Returns the instances of the class as a JSON string."""
        return json.dumps(cls.instances, ensure_ascii=False, indent=4)

    @classmethod
    def save_to_json(cls, filename: str) -> None:
        """Saves the instances of the class to a JSON file."""
        with open(filename, "w", encoding="utf-8") as f:
            json.dump(cls.instances, f, ensure_ascii=False, indent=4)