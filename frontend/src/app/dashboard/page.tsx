"use client";

import { useRoles } from "@/hooks/use-roles";
import { useRouter } from "next/navigation";
import { useEffect } from "react";

export default function DashboardPage() {
  const { isLoading, isOrganizer, isStaff } = useRoles();
  const router = useRouter();

  useEffect(() => {
    if (isLoading) return;

    if (isOrganizer) {
      router.push("/dashboard/events");
    } else if (isStaff) {
      router.push("/dashboard/validate-qr");
    } else {
      router.push("/dashboard/tickets");
    }
  }, [isLoading, isOrganizer, isStaff, router]);

  return <p>Loading...</p>;
}
