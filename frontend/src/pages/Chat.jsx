import { useState, useRef, useEffect } from 'react'
import client from '../api/axiosClient'


export default function Chat(){
const [messages, setMessages] = useState([])
const [input, setInput] = useState('')
const endRef = useRef()


useEffect(() => { endRef.current?.scrollIntoView({behavior:'smooth'}) }, [messages])


const send = async () => {
const text = input.trim()
if(!text) return
const userMsg = { id: Date.now()+':u', role:'user', content: text }
setMessages(m => [...m, userMsg])
setInput('')
try {
const { data } = await client.post('/chat', { message: text })
const aiMsg = { id: Date.now()+':a', role:'assistant', content: data.reply || String(data) }
setMessages(m => [...m, aiMsg])
} catch (e) {
setMessages(m => [...m, { id: Date.now()+':e', role:'system', content: 'Error reaching chat API' }])
}
}


return (
<div>
<h1>AI Chat</h1>
<div style={{border:'1px solid #ccc', padding:12, height:300, overflow:'auto'}}>
{messages.map(m => (
<div key={m.id} style={{marginBottom:8}}>
<strong>{m.role}:</strong> {m.content}
</div>
))}
<div ref={endRef} />
</div>
<div style={{marginTop:8}}>
<input value={input} onChange={e=>setInput(e.target.value)} onKeyDown={e=>e.key==='Enter' && send()} style={{width:300}} />
<button onClick={send} style={{marginLeft:8}}>Send</button>
</div>
</div>
)
}