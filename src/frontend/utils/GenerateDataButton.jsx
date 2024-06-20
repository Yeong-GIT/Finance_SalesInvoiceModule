import React from 'react';
import axios from 'axios';

const GenerateDataButton = ({ onGenerated }) => {
    const handleGenerateData = async () => {
        try {
            const response = await axios.get('http://localhost:8081/api/generate-data');  // Using the exposed port 8081
            console.log('Generated Data:', response.data);
            if (onGenerated) {
                onGenerated();
            }
        } catch (error) {
            console.error('Error generating data:', error);
        }
    };

    return (
        <button onClick={handleGenerateData}>Generate Sales Invoices</button>
    );
};

export default GenerateDataButton;
