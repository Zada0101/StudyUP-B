import { useState } from 'react'


export default function Register(){
const [form, setForm] = useState({ username:'', email:'', password:'' })
const [errors, setErrors] = useState({})
const [serverMsg, setServerMsg] = useState('')
const [ok, setOk] = useState(false)


const onChange = e => setForm(f => ({...f, [e.target.name]: e.target.value}))


const submit = async (e) => {
e.preventDefault()
setErrors({}); setServerMsg(''); setOk(false)


const errs = {}
if(!form.username) errs.username = 'username is required'
if(!form.email) errs.email = 'email is required'
if(!form.password || form.password.length < 8) errs.password = 'password must be at least 8 characters'
if(Object.keys(errs).length){ setErrors(errs); return }


const res = await fetch('/api/auth/register', {
method: 'POST', headers: { 'Content-Type':'application/json' },
body: JSON.stringify(form)
})


if(res.status === 201){ setOk(true); setForm({username:'', email:'', password:''}); return }
if(res.status === 400){ const b = await res.json(); setErrors(b.fieldErrors||{}); setServerMsg(b.error||'Validation failed'); return }
setServerMsg('Unexpected error')
}


return (
<div>
<h1>Register</h1>
{ok && <div className="ok">Registration successful</div>}
{serverMsg && <div className="err">{serverMsg}</div>}
<form onSubmit={submit}>
<div>
<label>Username</label>
<input name="username" value={form.username} onChange={onChange} />
{errors.username && <div className="err">{errors.username}</div>}
</div>
<div>
<label>Email</label>
<input name="email" type="email" value={form.email} onChange={onChange} />
{errors.email && <div className="err">{errors.email}</div>}
</div>
<div>
<label>Password</label>
<input name="password" type="password" value={form.password} onChange={onChange} />
{errors.password && <div className="err">{errors.password}</div>}
</div>
<button>Create account</button>
</form>
</div>
)
}