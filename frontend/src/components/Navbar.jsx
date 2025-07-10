import { Link, useNavigate } from "react-router-dom";
import { useAppContext } from "../contexts/AppContext";
import { User, Users, UserCog, LogOut } from "lucide-react";
import { useState, useRef, useEffect } from "react";

const Navbar = () => {
  const { accessToken, jwtPayload, logout } = useAppContext();
  const navigate = useNavigate();
  const [dropdownOpen, setDropdownOpen] = useState(false);
  const dropdownRef = useRef(null);

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  const handleSettings = () => {
    navigate("/profile");
    setDropdownOpen(false);
  };

  const handleClickOutside = (e) => {
    if (dropdownRef.current && !dropdownRef.current.contains(e.target)) {
      setDropdownOpen(false);
    }
  };

  useEffect(() => {
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  if (!accessToken) return null;

  return (
    <nav className="bg-indigo-600 text-white shadow">
      <div className="max-w-7xl mx-auto px-6 py-3 flex justify-between items-center">
        {/* Left: Logo */}
        <div className="flex items-center space-x-3">
          <img src="/logo.png" alt="Logo" className="w-8" />
          <span className="text-lg font-semibold tracking-wide">User Auth</span>
        </div>

        {/* Right: Menu and Avatar */}
        <div className="flex items-center space-x-10">
          <Link
            to="/profile"
            className="flex items-center space-x-1 text-white hover:text-indigo-50 transition"
          >
            <User className="w-5 h-5" />
            <span className="text-sm font-medium">Profile</span>
          </Link>

          {jwtPayload?.role === "ADMIN" && (
            <Link
              to="/dashboard"
              className="flex items-center space-x-1 hover:text-indigo-50 transition"
            >
              <Users className="w-5 h-5" />
              <span className="text-sm font-medium">Dashboard</span>
            </Link>
          )}

          {/* Avatar Dropdown */}
          <div className="relative" ref={dropdownRef}>
            <button
              onClick={() => setDropdownOpen(!dropdownOpen)}
              className="focus:outline-none"
            >
              <img
                src="https://www.paralysistreatments.com/wp-content/uploads/2018/02/no_profile_img.png"
                alt="Profile"
                className="w-9 h-9 rounded-full border-2 border-white shadow-sm"
              />
            </button>

            {dropdownOpen && (
              <div className="absolute right-0 mt-2 w-40 bg-white rounded-lg shadow-lg py-2 z-50">
                <button
                  onClick={handleSettings}
                  className="flex items-center space-x-2 w-full px-4 py-2 text-sm text-gray-700 hover:bg-gray-50"
                >
                  <UserCog className="w-4 h-4" />
                  <span>Settings</span>
                </button>
                <button
                  onClick={handleLogout}
                  className="flex items-center space-x-2 w-full px-4 py-2 text-sm text-gray-700 hover:bg-gray-50"
                >
                  <LogOut className="w-4 h-4" />
                  <span>Logout</span>
                </button>
              </div>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
