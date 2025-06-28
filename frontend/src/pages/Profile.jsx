import { useEffect, useState } from "react";
import { toast } from "react-toastify";
import { useAppContext } from "../contexts/AppContext";
import { getProfile } from "../api/userApi";
import Spinner from "../components/Spinner";
import EditProfileModal from "../components/EditProfileModal";
import {
  Mail,
  Phone,
  Calendar,
  MapPin,
  User,
  BadgeCheck,
  Venus,
  AlertCircle,
} from "lucide-react";

const ProfileItem = ({ icon, label, value }) => (
  <div className="flex items-center space-x-3">
    <div className="text-indigo-600">{icon}</div>
    <div>
      <p className="text-xs text-gray-500">{label}</p>
      <p className="font-medium text-gray-800">{value}</p>
    </div>
  </div>
);

const Profile = () => {
  const { accessToken } = useAppContext();
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [openEdit, setOpenEdit] = useState(false);

  const fetchProfile = async () => {
    setLoading(true);
    try {
      const res = await getProfile(accessToken);
      if (res.data.success) {
        setProfile(res.data.data);
      }
    } catch (err) {
      toast.error("Failed to fetch profile");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchProfile();
  }, []);

  const displayName =
    profile?.firstName || profile?.lastName
      ? `${profile?.firstName || ""} ${profile?.lastName || ""}`.trim()
      : profile?.username.toUpperCase();

  return (
    <div className="max-w-5xl mx-auto p-6">
      {loading ? (
        <Spinner />
      ) : profile ? (
        <div className="bg-gradient-to-br from-white to-gray-50 rounded-3xl shadow-2xl overflow-hidden border border-gray-200 relative">
          {/* Cover */}
          <div className="h-48 bg-gradient-to-r from-blue-600 to-purple-600 relative">
            <img
              src="https://wallpapers.com/images/hd/professional-background-0rvi0y9hjbs1yq1q.jpg"
              alt="Cover"
              className="object-cover w-full h-full"
            />
            {/* <div className="absolute inset-0 bg-black bg-opacity-30 backdrop-blur-sm"></div> */}
            <div className="absolute -bottom-16 left-8 flex items-center space-x-6">
              <img
                src="https://www.paralysistreatments.com/wp-content/uploads/2018/02/no_profile_img.png"
                className="w-36 h-36 rounded-full border-4 border-white shadow-lg object-cover"
                alt="avatar"
              />
              <div>
                <div className="flex items-center space-x-3  mb-10">
                  <h2 className="text-3xl font-bold text-white drop-shadow-lg">
                    {displayName}
                  </h2>
                  <span
                    className={`text-xs px-3 py-1 rounded-full ${
                      profile.role === "ADMIN" ? "bg-indigo-500" : "bg-green-500"
                    } text-white`}
                  >
                    {profile.role}
                  </span>
                </div>
              </div>
            </div>
          </div>

          {/* Details */}
          <div className="pt-24 pb-8 px-8 space-y-8">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <ProfileItem
                icon={<User className="w-5 h-5" />}
                label="Username"
                value={profile.username}
              />
              <ProfileItem
                icon={<Mail className="w-5 h-5" />}
                label="Email"
                value={profile.email}
              />
              <ProfileItem
                icon={<Phone className="w-5 h-5" />}
                label="Phone"
                value={profile.phone || "N/A"}
              />
              <ProfileItem
                icon={<Calendar className="w-5 h-5" />}
                label="Date of Birth"
                value={profile.dob || "N/A"}
              />
              <ProfileItem
                icon={<Venus className="w-5 h-5" />}
                label="Gender"
                value={profile.gender || "N/A"}
              />
              <ProfileItem
                icon={<MapPin className="w-5 h-5" />}
                label="Address"
                value={profile.address || "N/A"}
              />
              <ProfileItem
                icon={<BadgeCheck className="w-5 h-5" />}
                label="Status"
                value={profile.status}
              />
            </div>

            <div className="flex justify-end">
              <button
                onClick={() => setOpenEdit(true)}
                className="px-6 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition font-medium shadow-sm"
              >
                Edit Profile
              </button>
            </div>
          </div>
        </div>
      ) : (
        <div className="flex flex-col items-center justify-center text-center py-20 text-gray-500">
          <AlertCircle className="w-16 h-16 text-indigo-500 animate-bounce mb-4" />
          <p className="text-lg font-semibold">Oops! No profile found.</p>
          <p className="text-sm mt-1">
            Looks like something went missing or isn’t set up yet.
          </p>
        </div>
      )}

      {openEdit && (
        <EditProfileModal
          profile={profile}
          onClose={() => setOpenEdit(false)}
          onSave={fetchProfile}
        />
      )}
    </div>
  );
};

export default Profile;
