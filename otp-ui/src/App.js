import { BrowserRouter, Routes, Route } from "react-router-dom";
import RequestCode from "./pages/RequestCode";
import VerifyCode from "./pages/VerifyCode";
import Profile from "./pages/Profile";

function App() {
  return (
    <BrowserRouter>
      <div className="app-container">
        <Routes>
          <Route path="/" element={<RequestCode />} />
          <Route path="/verify" element={<VerifyCode />} />
          <Route path="/me" element={<Profile />} />
        </Routes>
      </div>
    </BrowserRouter>
  );
}

export default App;
