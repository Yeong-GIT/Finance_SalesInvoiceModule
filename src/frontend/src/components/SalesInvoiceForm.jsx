import { useState, useEffect } from "react";

const SalesInvoiceForm = ({onSubmit, selectedInvoice, setSelectedInvoice}) => {

    const [customerName, setCustomerName] = useState('');
    const [amount, setAmount] = useState('');
    const [invoiceDate, setInvoiceDate] = useState('');

    useEffect(() => {
        if(selectedInvoice){
            setCustomerName(selectedInvoice.customerName);
            setAmount(selectedInvoice.amount);
            setInvoiceDate(selectedInvoice.invoiceDate);
        }else{
            setCustomerName('');
            setAmount('');
            setInvoiceDate('');
        }
    }, [selectedInvoice]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        const invoice = { customerName, amount, invoiceDate};
        await onSubmit(invoice, selectedInvoice?.id);
        setCustomerName('');
        setAmount('');
        setInvoiceDate('');
        setSelectedInvoice(null);
    };

    return (
        <form onSubmit={handleSubmit}>
            <input type="text" value={customerName} onChange={(e) => setCustomerName(e.target.value)} placeholder="Customer Name" />
            <input type="number" value={amount} onChange={(e) => setAmount(e.target.value)} placeholder="Amount" />
            <input type="date" value={invoiceDate} onChange={(e) => setInvoiceDate(e.target.value)} placeholder="Invoice Date" />
            <button type = "submit">{selectedInvoice ? 'Update' : 'Add'} Sales Invoice</button>
        </form>
    );
};

export default SalesInvoiceForm;