import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Statistic from "../pages/Statistic";

export default function RoutesApp() {
  return (
    <Router>
      <Routes>
        <Route exact path="/" element={<Statistic />} />
      </Routes>
    </Router>
  );
}
