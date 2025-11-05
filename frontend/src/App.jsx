import { Routes, Route, Link } from 'react-router-dom' // Router pieces
import ProtectedRoute from './components/ProtectedRoute' // Guarded route
import Chat from './pages/Chat' // Chat page
import Login from './pages/Login' // Login page
import Register from './pages/Register' // Register page


export default function App(){
return (
<div style={{padding:16}}>
<nav style={{display:'flex', gap:12, marginBottom:12}}>
<Link to="/">Home</Link>
<Link to="/register">Register</Link>
<Link to="/chat">Chat</Link>
<Link to="/login">Login</Link>
</nav>
<Routes>
<Route path="/" element={<h1>Home</h1>} />
<Route path="/register" element={<Register />} />
<Route path="/login" element={<Login />} />
<Route path="/chat" element={<ProtectedRoute><Chat /></ProtectedRoute>} />
</Routes>
</div>
)
}