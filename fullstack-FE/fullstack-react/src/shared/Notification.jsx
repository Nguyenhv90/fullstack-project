import { useToast } from '@chakra-ui/react'


const toast = useToast()
const statuses = ['success', 'error', 'warning', 'info']
const Notification = (() => {
    return (
        <Wrap>
            {statuses.map((status, i) => (
                <WrapItem key={i}>
                    <Button
                        onClick={() =>
                            toast({
                                title: `${status} toast`,
                                status: status,
                                isClosable: true,
                            })
                        }
                    >
                        Show {status} toast
                    </Button>
                </WrapItem>
            ))}
        </Wrap>
    )
})

export default Notification;
