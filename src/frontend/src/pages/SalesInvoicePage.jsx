const SalesInvoicePage = () => {
    return(
        <div>
            <h1>Sales Invoices</h1>
            {/* <input type="text" /> */}
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
            </table>
        </div>
    )
};

export default SalesInvoicePage;