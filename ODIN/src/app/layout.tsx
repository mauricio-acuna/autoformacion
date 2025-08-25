import type { Metadata } from 'next';
import { Inter } from 'next/font/google';
import './globals.css';

const inter = Inter({ subsets: ['latin'] });

export const metadata: Metadata = {
  title: 'ODIN - Plataforma Inmobiliaria con IA',
  description: 'La plataforma inmobiliaria más avanzada de España con inteligencia artificial',
  keywords: 'inmobiliaria, propiedades, IA, inteligencia artificial, España',
  authors: [{ name: 'ODIN Team' }],
  creator: 'ODIN',
  openGraph: {
    title: 'ODIN - Plataforma Inmobiliaria con IA',
    description: 'La plataforma inmobiliaria más avanzada de España con inteligencia artificial',
    type: 'website',
    locale: 'es_ES',
  },
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="es" suppressHydrationWarning>
      <body className={inter.className}>
        <main>{children}</main>
      </body>
    </html>
  );
}
