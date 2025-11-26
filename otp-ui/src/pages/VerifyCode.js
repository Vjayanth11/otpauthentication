import { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function VerifyCode() {
  const [identifier, setIdentifier] = useState("");
  const [code, setCode] = useState("");
  const [response, setResponse] = useState("");
  const navigate = useNavigate();

  const verifyCode = async () => {
    const res = await fetch("http://localhost:8080/access/verify", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ identifier, code })
    });

    const json = await res.json();
    setResponse(JSON.stringify(json));

    if (json.token) {
      localStorage.setItem("token", json.token);
      setTimeout(() => navigate("/me"), 1200);
    }
  };

  return (
    <div className="card">
      <h2>Verify OTP</h2>

      <input
        className="input"
        placeholder="Identifier"
        value={identifier}
        onChange={(e) => setIdentifier(e.target.value)}
      />

      <input
        className="input"
        placeholder="Enter OTP"
        value={code}
        onChange={(e) => setCode(e.target.value)}
      />

      <button className="button" onClick={verifyCode}>
        Verify
      </button>

      <pre className="message">{response}</pre>
    </div>
  );
}
