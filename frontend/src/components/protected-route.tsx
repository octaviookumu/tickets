"use client";

import { ReactNode, useEffect } from "react";
import { useAuth } from "react-oidc-context";
import { useRouter, usePathname, useSearchParams } from "next/navigation";

interface ProtectedRouteProperties {
  children: ReactNode;
}

const ProtectedRoute: React.FC<ProtectedRouteProperties> = ({ children }) => {
  const { isLoading, isAuthenticated } = useAuth();
  const router = useRouter();
  const pathname = usePathname();
  const searchParams = useSearchParams();

  useEffect(() => {
    if (!isLoading && !isAuthenticated) {
      const search = searchParams.toString();
      localStorage.setItem(
        "redirectPath",
        pathname + (search ? `?${search}` : ""),
      );
      router.replace("/login");
    }
  }, [isLoading, isAuthenticated, router, pathname, searchParams]);

  if (isLoading || !isAuthenticated) {
    return <p>Loading...</p>;
  }

  return children;
};

export default ProtectedRoute;
