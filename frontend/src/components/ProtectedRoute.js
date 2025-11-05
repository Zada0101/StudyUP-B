import { Navigate, useLocation } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'


export default function ProtectedRoute({children}){
const { token } = useAuth() // Read token
const location = useLocation() // Current path
if(!token){ // If not logged in
return (
<Navigate to="/login" state={{ from: location.pathname, reason: 'You must be logged in to access the chat.' }} replace />
)
}
return children // If logged in, render content
}