"use client";

import ManageEventForm from "@/components/manage-event-form";
import { useParams } from "next/navigation";

export default function UpdateEventPage() {
  const params = useParams<{ id: string }>();
  return <ManageEventForm eventId={params.id} />;
}
