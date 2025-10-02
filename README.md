# Material Data Scraper and Viewer

This project consists of two main applications designed to scrape material data from a supplier's website and display it through a web interface.

1.  **Material FastAPI (Python)**: A web scraping service that extracts material data and saves it to a PostgreSQL database.
2.  **Material Spring Boot (Java)**: A web application that retrieves the data from the PostgreSQL database and displays it to the user using JSP pages.

---

## 🏛️ Project Architecture

The architecture is straightforward: the Python scraper acts as the data producer, and the Java web application acts as the data consumer, with PostgreSQL serving as the central data store.

1.  The **FastAPI** application is triggered to scrape a target website using `BeautifulSoup` and `Requests`.
2.  The scraped data is then cleaned and stored in a **PostgreSQL** database.
3.  The **Spring Boot** application connects to the same PostgreSQL database using **JDBC**.
4.  It serves dynamic web pages using **JSP** to display the material data stored in the database.



---

## ✨ Features

* **Automated Web Scraping**: Efficiently scrapes material data using a Python-based service.
* **Centralized Database**: Uses PostgreSQL for robust and reliable data storage.
* **Dynamic Web Interface**: A Java Spring Boot application displays the scraped data using JSP (JavaServer Pages).
* **JDBC Connectivity**: Employs standard JDBC for database interaction in the Spring Boot application, ensuring solid and controlled database communication.

---

## 🛠️ Technologies Used

### Backend (Material Spring Boot)

* **Java 17+**
* **Spring Boot**
* **Spring Web**
* **JDBC (Java Database Connectivity)**
* **JSP (JavaServer Pages)**
* **PostgreSQL JDBC Driver**
* **Maven** (for dependency management)

### Scraper (Material FastAPI)

* **Python 3.9+**
* **FastAPI**
* **Uvicorn**
* **BeautifulSoup4**
* **Requests**
* **Pandas**

### Database

* **PostgreSQL**

---

## 🚀 Getting Started

Follow these instructions to get the project up and running on your local machine.

### Prerequisites

Ensure you have the following installed:
* Java JDK (17 or higher)
* Apache Maven
* Python (3.9 or higher) & Pip
* PostgreSQL Server