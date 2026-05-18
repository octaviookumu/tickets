"use client";

import { useEffect } from "react";
import { useAuth } from "react-oidc-context";
import { useRouter } from "next/navigation";

export default function CallbackPage() {
  const { isAuthenticated, isLoading, error } = useAuth();
  const router = useRouter();

  useEffect(() => {
    if (isAuthenticated) {
      const redirectPath = localStorage.getItem("redirectPath") || "/";
      localStorage.removeItem("redirectPath");
      router.push(redirectPath);
    }
  }, [isAuthenticated, router]);

  if (isLoading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="w-12 h-12 border-4 border-white border-t-transparent rounded-full animate-spin"></div>
      </div>
    );
  }

  if (error) {
    return <p className="text-red-500">Login failed: {error.message}</p>;
  }

  return <p className="text-white text-center mt-20">Completing login...</p>;
}
