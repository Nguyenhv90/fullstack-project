import axios from 'axios';

export const getCustomers = async () => {
    try {
        return await axios.get("/api/v1/customers")
    } catch (e) {
        throw e;
    }

}