from faker import Faker
import random
import requests

faker = Faker()

def generate_sales_invoice():
    invoice = {
        "customerName": faker.name(),
        "amount": round(random.uniform(10.0, 1000.0), 2),
        "invoiceDate": faker.date_this_year().isoformat()
    }
    print("Generated Invoice:", invoice)
    return invoice

def post_sales_invoice():
    invoice = generate_sales_invoice()
    # Use the service name and internal port since the script runs within Docker's network.
    response = requests.post('http://sales-invoice-service:8080/api/invoices', json=invoice)
    print("Response:", response.json())
    return response.json()

if __name__ == "__main__":
    for _ in range(10):
        print(post_sales_invoice())
