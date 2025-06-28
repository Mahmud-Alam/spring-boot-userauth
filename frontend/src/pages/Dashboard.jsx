import { useEffect, useState } from "react";
import { toast } from "react-toastify";
import { getAllUsers } from "../api/userApi";
import { useAppContext } from "../contexts/AppContext";
import Spinner from "../components/Spinner";
import {
  Users,
  CheckCircle,
  XCircle,
  PauseCircle,
  UserCheck,
  UserX,
  Ban,
} from "lucide-react";

const Dashboard = () => {
  const { accessToken } = useAppContext();
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);

  const fetchUsers = async () => {
    setLoading(true);
    try {
      const res = await getAllUsers(accessToken);
      setUsers(res.data.data);
    } catch (err) {
      toast.error("Failed to load users");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  const total = users.length;
  const active = users.filter((u) => u.status === "ACTIVE").length;
  const inactive = users.filter((u) => u.status === "INACTIVE").length;
  const suspended = users.filter((u) => u.status === "SUSPENDED").length;

  const Card = ({ icon, label, value }) => (
    <div className="flex items-center space-x-4 bg-white rounded-xl shadow p-5 border border-gray-100">
      <div>{icon}</div>
      <div>
        <h3 className="text-gray-600 text-sm">{label}</h3>
        <p className="text-2xl font-bold text-gray-800">{value}</p>
      </div>
    </div>
  );

  return (
    <div className="max-w-7xl mx-auto p-8 space-y-10">
      <h1 className="text-4xl font-bold text-gray-800">Admin Dashboard</h1>

      <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
        <Card
          // color="bg-blue-50"
          icon={<Users className="w-8 h-8 text-blue-500" />}
          label="Total Users"
          value={total}
        />
        <Card
          // color="bg-green-50"
          icon={<UserCheck className="w-8 h-8 text-green-500" />}
          label="Active"
          value={active}
        />
        <Card
          // color="bg-yellow-50"
          icon={<UserX className="w-8 h-8 text-yellow-500" />}
          label="Inactive"
          value={inactive}
        />
        <Card
          // color="bg-red-50"
          icon={<Ban className="w-8 h-8 text-red-500" />}
          label="Suspended"
          value={suspended}
        />
      </div>

      <div>
        <h2 className="text-2xl font-semibold text-gray-700 mb-4">
          Recent Users
        </h2>

        {loading ? (
          <Spinner />
        ) : (
          <div className="overflow-x-auto">
            <table className="min-w-full bg-white border border-gray-200 rounded-lg shadow">
              <thead>
                <tr className="bg-gray-50 text-left text-sm text-gray-600">
                  <th className="p-4">Username</th>
                  <th className="p-4">Email</th>
                  <th className="p-4">Role</th>
                  <th className="p-4">Status</th>
                </tr>
              </thead>
              <tbody>
                {users.map((u, index) => (
                  <tr
                    key={u.id}
                    className={`text-sm ${
                      index % 2 === 0 ? "bg-white" : "bg-gray-50"
                    } hover:bg-blue-50 transition`}
                  >
                    <td className="p-4 font-medium text-gray-800">
                      {u.username}
                    </td>
                    <td className="p-4">{u.email}</td>
                    <td className="p-4">
                      <span
                        className={`text-xs font-semibold px-3 py-1 rounded-full ${
                          u.role === "ADMIN"
                            ? "bg-red-100 text-red-600"
                            : "bg-green-100 text-green-600"
                        }`}
                      >
                        {u.role}
                      </span>
                    </td>
                    <td className="p-4">
                      <span
                        className={`px-3 py-1 rounded-full text-xs font-medium ${
                          u.status === "ACTIVE"
                            ? "bg-green-100 text-green-700"
                            : u.status === "INACTIVE"
                            ? "bg-yellow-100 text-yellow-700"
                            : "bg-red-100 text-red-700"
                        }`}
                      >
                        {u.status}
                      </span>
                    </td>
                  </tr>
                ))}
                {users.length === 0 && (
                  <tr>
                    <td colSpan="4" className="p-4 text-center text-gray-500">
                      No users found.
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
};

export default Dashboard;
