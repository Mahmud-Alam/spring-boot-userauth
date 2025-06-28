import { useState } from "react";
import { toast } from "react-toastify";
import { useAppContext } from "../contexts/AppContext";
import { updateProfile } from "../api/userApi";
import {
  User,
  Phone,
  Calendar,
  MapPin,
  X,
  Save,
  UserCircle2,
} from "lucide-react";

const EditProfileModal = ({ profile, onClose, onSave }) => {
  const { accessToken } = useAppContext();
  const [form, setForm] = useState({ ...profile });
  const [loading, setLoading] = useState(false);

  const handleChange = (e) =>
    setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      await updateProfile(form);
      toast.success("Profile updated!");
      onClose();
      onSave();
    } catch (err) {
      toast.error("Failed to update profile");
    } finally {
      setLoading(false);
    }
  };

  const InputField = ({ icon, ...props }) => (
    <div className="flex items-center border rounded-lg px-3 py-2 bg-white shadow-sm focus-within:ring-2 focus-within:ring-blue-100">
      <div className="text-gray-400 mr-2">{icon}</div>
      <input
        {...props}
        className="flex-1 outline-none text-gray-800 placeholder-gray-400"
      />
    </div>
  );

  return (
    <div className="fixed inset-0 backdrop-blur-sm bg-opacity-30 flex items-center justify-center z-50">
      <div className="bg-white rounded-2xl w-full max-w-md p-6 space-y-5 border border-gray-200 shadow-xl relative">
        {/* Close button */}
        <button
          onClick={onClose}
          className="absolute top-3 right-3 p-1 border text-indigo-600 hover:bg-indigo-600 hover:text-white rounded-full transition duration-200 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
        >
          <X className="w-5 h-5" />
        </button>

        {/* Title */}
        <div className="flex items-center space-x-2 mb-4">
          <UserCircle2 className="w-7 h-7 text-indigo-600" />
          <h2 className="text-xl font-bold text-gray-700">Edit Profile</h2>
        </div>

        <form onSubmit={handleSubmit} className="space-y-3">
          <InputField
            icon={<User className="w-5 h-5" />}
            name="firstName"
            value={form.firstName || ""}
            onChange={handleChange}
            placeholder="First Name"
          />
          <InputField
            icon={<User className="w-5 h-5" />}
            name="lastName"
            value={form.lastName || ""}
            onChange={handleChange}
            placeholder="Last Name"
          />
          <InputField
            icon={<Phone className="w-5 h-5" />}
            name="phone"
            value={form.phone || ""}
            onChange={handleChange}
            placeholder="Phone Number"
          />
          <InputField
            icon={<Calendar className="w-5 h-5" />}
            name="dob"
            type="date"
            value={form.dob || ""}
            onChange={handleChange}
          />
          <InputField
            icon={<MapPin className="w-5 h-5" />}
            name="address"
            value={form.address || ""}
            onChange={handleChange}
            placeholder="Address"
          />

          {/* Buttons */}
          <div className="flex justify-end pt-3 space-x-3">
            <button
              type="button"
              onClick={onClose}
              className="flex items-center px-4 py-2 border border-gray-300 text-gray-600 rounded-lg hover:bg-gray-100 transition"
            >
              <X className="w-4 h-4 mr-1" />
              Cancel
            </button>
            <button
              type="submit"
              disabled={loading}
              className="flex items-center px-4 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition"
            >
              <Save className="w-4 h-4 mr-1" />
              {loading ? "Saving..." : "Save"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default EditProfileModal;
