export interface User {
  id: string;
  email: string;
  name?: string;
  image?: string;
  role: 'USER' | 'AGENT' | 'ADMIN';
  createdAt: Date;
  updatedAt: Date;
}

export interface Property {
  id: string;
  title: string;
  description: string;
  price: number;
  currency: string;
  type: PropertyType;
  category: PropertyCategory;
  status: PropertyStatus;
  
  // Location
  address: string;
  city: string;
  province: string;
  postalCode: string;
  latitude?: number;
  longitude?: number;
  
  // Details
  bedrooms?: number;
  bathrooms?: number;
  area?: number;
  yearBuilt?: number;
  
  // AI Generated
  aiDescription?: string;
  aiValuation?: number;
  
  // Media
  images: PropertyImage[];
  virtualTour?: string;
  
  // Metadata
  featured: boolean;
  views: number;
  createdAt: Date;
  updatedAt: Date;
  
  // Relations
  ownerId: string;
  owner: User;
}

export interface PropertyImage {
  id: string;
  url: string;
  alt?: string;
  order: number;
  propertyId: string;
  createdAt: Date;
}

export type PropertyType = 
  | 'APARTMENT'
  | 'HOUSE'
  | 'VILLA'
  | 'TOWNHOUSE'
  | 'PENTHOUSE'
  | 'STUDIO'
  | 'DUPLEX'
  | 'COMMERCIAL'
  | 'OFFICE'
  | 'WAREHOUSE'
  | 'LAND';

export type PropertyCategory = 
  | 'SALE'
  | 'RENT'
  | 'VACATION_RENTAL';

export type PropertyStatus = 
  | 'AVAILABLE'
  | 'SOLD'
  | 'RENTED'
  | 'RESERVED'
  | 'UNDER_CONSTRUCTION';

export interface SearchFilters {
  type?: PropertyType;
  category?: PropertyCategory;
  minPrice?: number;
  maxPrice?: number;
  bedrooms?: number;
  bathrooms?: number;
  minArea?: number;
  maxArea?: number;
  city?: string;
  province?: string;
  features?: string[];
}
