export interface EventDTO {
  name: string;
  description: string;
  maxParticipants: number;
  privacyType: string;
  location: string;
  eventDate?: string;
  eventTypeId?: string;
}
