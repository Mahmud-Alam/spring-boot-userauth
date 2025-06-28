import { Navigate } from "react-router-dom";
import { useAppContext } from "../contexts/AppContext";

const PrivateRoute = ({ children, role }) => {
  const { accessToken, jwtPayload } = useAppContext();

  if (!accessToken) {
    return <Navigate to="/login" />;
  }

  if (role && jwtPayload?.role !== role) {
    return (
      <div className="text-center p-5 text-red-600">You are not authorized</div>
    );
  }

  return children;
};

export default PrivateRoute;
