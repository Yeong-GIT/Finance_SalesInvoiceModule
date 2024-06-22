import React, { useState, useEffect, useRef } from 'react';
import { createSalesInvoice, getAllSalesInvoices, updateSalesInvoice, deleteSalesInvoice } from '../services/SalesInvoiceService';
import GenerateDataButton from '../../utils/GenerateDataButton';

const SalesInvoicePage = () => {
    const [invoices, setInvoices] = useState([]);
    const [selectedInvoice, setSelectedInvoice] = useState(null);
    const tableRef = useRef(null);

    useEffect(() => {
        const fetchInvoices = async () => {
            const response = await getAllSalesInvoices();
            setInvoices(response.data);

            if (!$.fn.dataTable.isDataTable('#salesInvoicesTable')) {
                tableRef.current = $('#salesInvoicesTable').DataTable({
                    data: response.data,
                    responsive: true,
                    paging: true,
                    pageLength: 10,
                    lengthChange: true,
                    lengthMenu: [5, 10, 25, 50],
                    searching: true,
                    info: true,
                    columns: [
                        { data: 'id' },
                        { data: 'customerName' },
                        { data: 'amount' },
                        { data: 'invoiceDate' },
                        {
                            data: null,
                            render: (data, type, row) => `
                                <button class="update-btn" data-id="${row.id}">Update</button>
                                <button class="delete-btn" data-id="${row.id}">Delete</button>
                            `
                        }
                    ],
                    destroy: true,
                    dom: 'lfrtip',
                    initComplete: function () {
                        $('#salesInvoicesTable_filter').prepend(`
                            <div style="display: flex; gap: 10px;">
                                <input type="text" id="customerNameInput" placeholder="Customer Name" style="width: 150px;" />
                                <input type="number" id="amountInput" placeholder="Amount" style="width: 100px;" />
                                <input type="date" id="invoiceDateInput" style="width: 150px;" />
                                <button id="addInvoiceBtn" class="btn btn-success">Add Sales Invoice</button>
                            </div>
                        `);

                        $('#addInvoiceBtn').on('click', async () => {
                            const customerName = $('#customerNameInput').val();
                            const amount = $('#amountInput').val();
                            const invoiceDate = $('#invoiceDateInput').val();

                            if (customerName && amount && invoiceDate) {
                                const newInvoice = {
                                    customerName,
                                    amount: parseFloat(amount),
                                    invoiceDate
                                };
                                await createSalesInvoice(newInvoice);
                                const response = await getAllSalesInvoices();
                                setInvoices(response.data);

                                // Clear the input fields after adding a new invoice
                                $('#customerNameInput').val('');
                                $('#amountInput').val('');
                                $('#invoiceDateInput').val('');
                            } else {
                                alert("Please fill in all fields.");
                            }
                        });
                    }
                });
            }
        };

        fetchInvoices();

        return () => {
            if ($.fn.dataTable.isDataTable('#salesInvoicesTable')) {
                $('#salesInvoicesTable').DataTable().destroy();
            }
        };
    }, []);

    useEffect(() => {
        if (tableRef.current) {
            tableRef.current.clear().rows.add(invoices).draw();

            $('#salesInvoicesTable tbody').off('click', '.update-btn').on('click', '.update-btn', function () {
                const id = $(this).data('id');
                const invoice = invoices.find(invoice => invoice.id === id);
                setSelectedInvoice(invoice);
                $('#customerNameInput').val(invoice.customerName);
                $('#amountInput').val(invoice.amount);
                $('#invoiceDateInput').val(invoice.invoiceDate);
                $('#addInvoiceBtn').text('Update Sales Invoice').off('click').on('click', async () => {
                    const updatedInvoice = {
                        customerName: $('#customerNameInput').val(),
                        amount: parseFloat($('#amountInput').val()),
                        invoiceDate: $('#invoiceDateInput').val()
                    };
                    if (updatedInvoice.customerName && updatedInvoice.amount && updatedInvoice.invoiceDate) {
                        await updateSalesInvoice(id, updatedInvoice);
                        setSelectedInvoice(null);
                        $('#addInvoiceBtn').text('Add Sales Invoice').off('click').on('click', async () => {
                            const customerName = $('#customerNameInput').val();
                            const amount = $('#amountInput').val();
                            const invoiceDate = $('#invoiceDateInput').val();

                            if (customerName && amount && invoiceDate) {
                                const newInvoice = {
                                    customerName,
                                    amount: parseFloat(amount),
                                    invoiceDate
                                };
                                await createSalesInvoice(newInvoice);
                                const response = await getAllSalesInvoices();
                                setInvoices(response.data);

                                $('#customerNameInput').val('');
                                $('#amountInput').val('');
                                $('#invoiceDateInput').val('');
                            } else {
                                alert("Please fill in all fields.");
                            }
                        });

                        const response = await getAllSalesInvoices();
                        setInvoices(response.data);

                        $('#customerNameInput').val('');
                        $('#amountInput').val('');
                        $('#invoiceDateInput').val('');
                    } else {
                        alert("Please fill in all fields.");
                    }
                });
            });

            $('#salesInvoicesTable tbody').off('click', '.delete-btn').on('click', '.delete-btn', async function () {
                const id = $(this).data('id');
                await deleteSalesInvoice(id);
                const response = await getAllSalesInvoices();
                setInvoices(response.data);
                tableRef.current.clear().rows.add(response.data).draw();
            });
        }
    }, [invoices]);

    const refreshInvoices = async () => {
        const response = await getAllSalesInvoices();
        setInvoices(response.data);
    };

    return (
        <div>
            <h1>Sales Invoices</h1>
            <GenerateDataButton onGenerated={refreshInvoices} />
            <table id="salesInvoicesTable" className="display responsive nowrap" style={{ width: '100%' }}>
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
                    {/* DataTables will handle data rendering */}
                </tbody>
            </table>
        </div>
    );
};

export default SalesInvoicePage;
