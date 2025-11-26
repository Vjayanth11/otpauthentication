import { useEffect, useState } from "react";

export default function Profile() {
  const [data, setData] = useState(null);

  useEffect(() => {
    const token = localStorage.getItem("token");

    fetch("http://localhost:8080/access/me", {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
      .then((res) => res.json())
      .then(setData);
  }, []);

  return (
    <div className="card">
      <h2>Your Profile</h2>
      <pre>{JSON.stringify(data, null, 2)}</pre>
    </div>
  );
}
