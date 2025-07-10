import { Navigate } from "react-router-dom";
import { useAppContext } from "../contexts/AppContext";
import Unauthorized from "../pages/Unauthorized";

const PrivateRoute = ({ children, role }) => {
  const { accessToken, jwtPayload } = useAppContext();

  if (!accessToken) {
    return <Navigate to="/login" />;
  }

  if (role && jwtPayload?.role !== role) {
    return <Unauthorized />;
  }

  return children;
};

export default PrivateRoute;
