import { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function RequestCode() {
  const [identifier, setIdentifier] = useState("");
  const [message, setMessage] = useState("");
  const navigate = useNavigate();

  const requestCode = async () => {
    const res = await fetch("http://localhost:8080/access/request", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ identifier })
    });

    const text = await res.text();
    setMessage(text);

    if (res.ok) {
      setTimeout(() => navigate("/verify"), 1200);
    }
  };

  return (
    <div className="card">
      <h2>OTP Login</h2>

      <input
        className="input"
        placeholder="Email or phone"
        value={identifier}
        onChange={(e) => setIdentifier(e.target.value)}
      />

      <button className="button" onClick={requestCode}>
        Send Code
      </button>

      <p className="message">{message}</p>
    </div>
  );
}
