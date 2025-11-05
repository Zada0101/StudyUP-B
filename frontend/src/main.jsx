import React from 'react' // React core
import { createRoot } from 'react-dom/client' // Mount React app
import { BrowserRouter } from 'react-router-dom' // Routing
import App from './App' // Main component
import { AuthProvider } from './context/AuthContext' // Auth state provider


createRoot(document.getElementById('root')).render(
<React.StrictMode>
<BrowserRouter>
<AuthProvider>
<App />
</AuthProvider>
</BrowserRouter>
</React.StrictMode>
)