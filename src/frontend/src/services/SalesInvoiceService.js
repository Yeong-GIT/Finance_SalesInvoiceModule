import axios from 'axios';

const API_URL = 'http://localhost:8080/api/invoices';

export const createSalesInvoice = async (invoice) => {
    return await axios.post(API_URL, invoice);
}

export const getAllSalesInvoice = async () => {
    return await axios.get(API_URL);
}

export const updateSalesInvoice = async (id, receipt) => {
    return await axios.put(`${API_URL}/${id}`, receipt);
}

export const deleteSalesInvoice = async (id) => {
    return await axios.delete(`${API_URL}/${id}`)
}