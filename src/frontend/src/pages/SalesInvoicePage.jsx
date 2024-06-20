import { useState, useEffect } from 'react';
import { createSalesInvoice, getAllSalesInvoices, updateSalesInvoice, deleteSalesInvoice } from '../services/SalesInvoiceService';
import SalesInvoiceForm from '../components/SalesInvoiceForm';
import GenerateDataButton from '../../utils/GenerateDataButton';

const SalesInvoicePage = () => {
    const [invoices, setInvoices] = useState([]);
    const [filteredInvoices, setFilteredInvoices] = useState([]);
    const [selectedInvoice, setSelectedInvoice] = useState(null);
    const [searchQuery, setSearchQuery] = useState('');

    useEffect(() => {
        const fetchInvoices = async () => {
            const response = await getAllSalesInvoices();
            setInvoices(response.data);
            setFilteredInvoices(response.data);
        };
        fetchInvoices();
    }, []);

    const handleSubmit = async (invoice, id) => {
        if (id) {
            await updateSalesInvoice(id, invoice);
        } else {
            await createSalesInvoice(invoice);
        }
        const response = await getAllSalesInvoices();
        setInvoices(response.data);
        setFilteredInvoices(response.data);
    };

    const handleUpdateClick = (invoice) => {
        setSelectedInvoice(invoice);
    };

    const handleDeleteClick = async (id) => {
        await deleteSalesInvoice(id);
        const response = await getAllSalesInvoices();
        setInvoices(response.data);
        setFilteredInvoices(response.data);
    };

    const handleSearch = (e) => {
        const query = e.target.value;
        setSearchQuery(query);
        if (query) {
            setFilteredInvoices(invoices.filter(invoice => invoice.customerName.toLowerCase().includes(query.toLowerCase())));
        } else {
            setFilteredInvoices(invoices);
        }
    };

    // Function to refresh invoices
    const refreshInvoices = async () => {
        const response = await getAllSalesInvoices();
        setInvoices(response.data);
        setFilteredInvoices(response.data);
    };

    return (
        <div>
            <h1>Sales Invoices</h1>
            <input
                type="text"
                placeholder="Search by Customer Name"
                value={searchQuery}
                onChange={handleSearch}
            />
            <SalesInvoiceForm onSubmit={handleSubmit} selectedInvoice={selectedInvoice} setSelectedInvoice={setSelectedInvoice} />
            <GenerateDataButton onGenerated={refreshInvoices} /> {/* Add the button here */}
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Customer Name</th>
                        <th>Amount</th>
                        <th>Invoice Date</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {filteredInvoices.map(invoice => (
                        <tr key={invoice.id}>
                            <td>{invoice.id}</td>
                            <td>{invoice.customerName}</td>
                            <td>{invoice.amount}</td>
                            <td>{invoice.invoiceDate}</td>
                            <td>
                                <button onClick={() => handleUpdateClick(invoice)}>Update</button>
                                <button onClick={() => handleDeleteClick(invoice.id)}>Delete</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default SalesInvoicePage;
