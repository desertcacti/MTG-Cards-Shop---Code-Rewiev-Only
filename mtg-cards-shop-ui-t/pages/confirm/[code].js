import { useRouter } from 'next/router';
import { useEffect, useState } from 'react';

const Confirm = () => {
    const router = useRouter();
    const { code } = router.query;
    const [message, setMessage] = useState('');

    useEffect(() => {
        if (code) {
            fetch(`http://localhost:8080/api/auth/confirm/${code}`)
                .then(response => response.json())
                .then(data => setMessage(data.message))
                .catch(err => setMessage('An error occurred while confirming the account.'));
        }
    }, [code]);

    return (
        <div>
            <h1>Confirmation</h1>
            <p>{message}</p>
        </div>
    );
};

export default Confirm;