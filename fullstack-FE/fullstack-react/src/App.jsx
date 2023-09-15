import { Button, Spinner, Text } from '@chakra-ui/react'
import SidebarWithHeader from "./shared/SideBar.jsx";
import {useEffect, useState} from "react";
import {getCustomers} from "./services/clients.js";
const App = () => {
    const [customers, setCustomers] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        setLoading(true);
        getCustomers().then(res => {
            setCustomers(res.data)
        }).catch(err => {
            console.log(err)
        }).finally( () => {
            setLoading(false);
        });
    }, [])

    if (loading) {
        return (
            <SidebarWithHeader>
                <Spinner
                    thickness='4px'
                    speed='0.65s'
                    emptyColor='gray.200'
                    color='blue.500'
                    size='xl'
                />
            </SidebarWithHeader>
        )
    }
    if (customers.length <= 0) {
        return (
        <SidebarWithHeader>
            <Text></Text>
        </SidebarWithHeader>
        )
    }

    return (
        <SidebarWithHeader>
            <Button colerScheme='teal' variant='outline'>Click me</Button>
        </SidebarWithHeader>

    )
}
export default  App;