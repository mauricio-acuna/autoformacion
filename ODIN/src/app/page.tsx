export default function Home() {
  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
      <div className="container mx-auto px-4 py-16">
        <div className="text-center max-w-4xl mx-auto">
          <h1 className="text-5xl font-bold text-gray-900 mb-6">
            Bienvenido a{' '}
            <span className="text-blue-600">ODIN</span>
          </h1>
          <p className="text-xl text-gray-600 mb-8">
            La plataforma inmobiliaria más avanzada de España con inteligencia artificial
          </p>
          <div className="grid md:grid-cols-3 gap-8 mt-12">
            <div className="bg-white p-6 rounded-lg shadow-md">
              <h3 className="text-xl font-semibold mb-3 text-gray-800">
                Búsqueda Inteligente
              </h3>
              <p className="text-gray-600">
                Encuentra tu propiedad ideal con nuestro sistema de IA avanzado
              </p>
            </div>
            <div className="bg-white p-6 rounded-lg shadow-md">
              <h3 className="text-xl font-semibold mb-3 text-gray-800">
                Tours Virtuales
              </h3>
              <p className="text-gray-600">
                Explora propiedades con tours 360° y realidad aumentada
              </p>
            </div>
            <div className="bg-white p-6 rounded-lg shadow-md">
              <h3 className="text-xl font-semibold mb-3 text-gray-800">
                Valoraciones IA
              </h3>
              <p className="text-gray-600">
                Obtén valoraciones precisas generadas por inteligencia artificial
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
