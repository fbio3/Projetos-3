import { useState, useEffect } from 'react';
import {
  MapContainer,
  TileLayer,
  Marker,
  Popup,
  useMap,
} from 'react-leaflet';
import L from 'leaflet';

const API_URL = import.meta.env.VITE_API_URL || '/api';

async function tratarResposta(resposta) {
  if (!resposta.ok) {
    const texto = await resposta.text();
    throw new Error(texto || `Erro ${resposta.status}`);
  }
  return resposta.json();
}

async function pesquisarVagas({ endereco, raioKm = 5 }) {
  const params = new URLSearchParams({ endereco, raioKm });
  const resposta = await fetch(`${API_URL}/vagas/pesquisar?${params}`);
  return tratarResposta(resposta);
}

async function vagasProximas({ lat, lng, raioKm = 5 }) {
  const params = new URLSearchParams({ lat, lng, raioKm });
  const resposta = await fetch(`${API_URL}/vagas/proximas?${params}`);
  return tratarResposta(resposta);
}

const formatarCoordenadas = (lat, lng) => {
  if (lat == null || lng == null) return '—';
  return `${lat.toFixed(5)}, ${lng.toFixed(5)}`;
};

const ABAS = [
  { id: 'buscar', label: 'Buscar vagas' },
  { id: 'filtrar', label: 'Filtrar vagas' },
];

function MenuAbas({ ativa, onChange }) {
  return (
    <nav className="abas" role="tablist" aria-label="Navegação principal">
      {ABAS.map((aba) => (
        <button
          key={aba.id}
          role="tab"
          aria-selected={ativa === aba.id}
          className={`aba ${ativa === aba.id ? 'aba--ativa' : ''}`}
          onClick={() => onChange(aba.id)}
        >
          {aba.label}
        </button>
      ))}
    </nav>
  );
}

function CardVaga({ vaga, onSelecionar }) {
  return (
    <li
      className="vaga"
      onClick={() => onSelecionar?.(vaga)}
      style={{ cursor: onSelecionar ? 'pointer' : 'default' }}
    >
      <div className="vaga__topo">
        <h2 className="vaga__endereco">{vaga.endereco}</h2>
        {vaga.pcd && <span className="vaga__tag">PCD</span>}
      </div>

      <p className="vaga__coord">
        {formatarCoordenadas(vaga.latitude, vaga.longitude)}
      </p>

      <div className="vaga__rodape">
        <span
          className={`vaga__status ${
            vaga.disponivel ? 'vaga__status--ok' : 'vaga__status--off'
          }`}
        >
          {vaga.disponivel ? 'Disponível' : 'Ocupada'}
        </span>
      </div>
    </li>
  );
}

delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl:
    'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon-2x.png',
  iconUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png',
  shadowUrl:
    'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
});

function VoarPara({ posicao }) {
  const map = useMap();
  useEffect(() => {
    if (posicao) {
      map.flyTo([posicao.lat, posicao.lng], 15, { duration: 1 });
    }
  }, [posicao, map]);
  return null;
}

function MapaLeaflet({
  vagas = [],
  centro = { lat: -8.0476, lng: -34.877 },
  zoom = 13,
  posicaoSelecionada = null,
}) {
  return (
    <div className="mapa-wrapper">
      <MapContainer
        center={[centro.lat, centro.lng]}
        zoom={zoom}
        style={{ height: '100%', width: '100%', borderRadius: '16px' }}
        scrollWheelZoom
      >
        <TileLayer
          attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        />

        <VoarPara posicao={posicaoSelecionada} />

        {vagas.map((vaga) => (
          <Marker
            key={vaga.id}
            position={[vaga.latitude, vaga.longitude]}
          >
            <Popup>
              <strong>{vaga.endereco}</strong>
              <br />
              {vaga.pcd && (
                <>
                  ♿ Vaga PCD
                  <br />
                </>
              )}
              {vaga.disponivel ? '✅ Disponível' : '❌ Ocupada'}
            </Popup>
          </Marker>
        ))}
      </MapContainer>
    </div>
  );
}

function FiltrosVaga({ filtros, onChange, raioKm, onRaioChange }) {
  function toggle(campo) {
    onChange({ ...filtros, [campo]: !filtros[campo] });
  }

  return (
    <div className="filtros">
      <h3 className="filtros__titulo">Filtrar Vagas</h3>

      <label className="filtros__check">
        <input
          type="checkbox"
          checked={filtros.somentePcd}
          onChange={() => toggle('somentePcd')}
        />
        <span>Vagas PCD</span>
      </label>

      <label className="filtros__check">
        <input
          type="checkbox"
          checked={filtros.somenteDisponiveis}
          onChange={() => toggle('somenteDisponiveis')}
        />
        <span>Somente disponíveis</span>
      </label>

      <div className="filtros__grupo">
        <label htmlFor="raio">Raio de busca</label>
        <select
          id="raio"
          value={raioKm}
          onChange={(e) => onRaioChange(Number(e.target.value))}
        >
          <option value={1}>1 km</option>
          <option value={2}>2 km</option>
          <option value={5}>5 km</option>
          <option value={10}>10 km</option>
          <option value={20}>20 km</option>
        </select>
      </div>
    </div>
  );
}

function BuscarVagas() {
  const [endereco, setEndereco] = useState('');
  const [vagas, setVagas] = useState([]);
  const [status, setStatus] = useState('parado');
  const [mensagem, setMensagem] = useState('');
  const [selecionada, setSelecionada] = useState(null);

  async function aoEnviar(e) {
    e.preventDefault();
    if (!endereco.trim()) {
      setStatus('erro');
      setMensagem('Digite um endereço para buscar.');
      return;
    }

    setStatus('carregando');
    setMensagem('Buscando vagas...');
    setVagas([]);
    setSelecionada(null);

    try {
      const dados = await pesquisarVagas({
        endereco: endereco.trim(),
        raioKm: 5,
      });
      setVagas(dados);
      setStatus('ok');
      setMensagem(dados.length === 0 ? 'Nenhuma vaga encontrada.' : '');

      if (dados.length > 0) {
        setSelecionada({
          lat: dados[0].latitude,
          lng: dados[0].longitude,
        });
      }
    } catch (erro) {
      setStatus('erro');
      setMensagem(erro.message || 'Erro ao buscar vagas.');
    }
  }

  function selecionar(vaga) {
    setSelecionada({ lat: vaga.latitude, lng: vaga.longitude });
  }

  return (
    <section className="pagina">
      <form onSubmit={aoEnviar} className="busca">
        <div className="campo">
          <svg
            className="campo__icone"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            strokeWidth="2"
            strokeLinecap="round"
            strokeLinejoin="round"
            aria-hidden="true"
          >
            <path d="M12 21s-7-6.2-7-11.5A7 7 0 0 1 19 9.5C19 14.8 12 21 12 21z" />
            <circle cx="12" cy="9.5" r="2.5" />
          </svg>
          <label htmlFor="endereco" className="somente-leitor">
            Endereço
          </label>
          <input
            id="endereco"
            className="campo__input"
            type="text"
            value={endereco}
            onChange={(e) => setEndereco(e.target.value)}
            placeholder="Digite uma localização para encontrar vagas próximas"
            autoComplete="street-address"
          />
        </div>

        <button type="submit" className="botao botao--primario">
          Buscar vagas
        </button>
      </form>

      {mensagem && (
        <p
          className={`status ${status === 'erro' ? 'status--erro' : ''}`}
          role="status"
          aria-live="polite"
        >
          {mensagem}
        </p>
      )}

      <div className="layout-busca">
        <MapaLeaflet
          vagas={vagas}
          posicaoSelecionada={selecionada}
          centro={selecionada || undefined}
        />

        {vagas.length > 0 && (
          <ul className="lista">
            {vagas.map((vaga) => (
              <CardVaga
                key={vaga.id}
                vaga={vaga}
                onSelecionar={selecionar}
              />
            ))}
          </ul>
        )}
      </div>
    </section>
  );
}

function FiltrarVagas() {
  const [endereco, setEndereco] = useState('');
  const [raioKm, setRaioKm] = useState(5);
  const [filtros, setFiltros] = useState({
    somentePcd: false,
    somenteDisponiveis: false,
  });

  const [vagas, setVagas] = useState([]);
  const [status, setStatus] = useState('parado');
  const [mensagem, setMensagem] = useState('');
  const [selecionada, setSelecionada] = useState(null);

  function aplicarFiltros(lista) {
    return lista.filter((v) => {
      if (filtros.somentePcd && !v.pcd) return false;
      if (filtros.somenteDisponiveis && !v.disponivel) return false;
      return true;
    });
  }

  async function buscarPorEndereco(e) {
    e?.preventDefault();
    if (!endereco.trim()) {
      setStatus('erro');
      setMensagem('Digite um endereço para buscar.');
      return;
    }

    setStatus('carregando');
    setMensagem('Buscando vagas...');
    setVagas([]);

    try {
      const dados = await pesquisarVagas({
        endereco: endereco.trim(),
        raioKm,
      });
      const filtradas = aplicarFiltros(dados);
      setVagas(filtradas);
      setStatus('ok');
      setMensagem(
        filtradas.length === 0
          ? 'Nenhuma vaga encontrada com esses filtros.'
          : `${filtradas.length} vaga(s) encontrada(s).`
      );
      if (filtradas.length > 0) {
        setSelecionada({
          lat: filtradas[0].latitude,
          lng: filtradas[0].longitude,
        });
      }
    } catch (erro) {
      setStatus('erro');
      setMensagem(erro.message || 'Erro ao buscar vagas.');
    }
  }

  async function buscarProximas() {
    if (!('geolocation' in navigator)) {
      setStatus('erro');
      setMensagem('Seu navegador não suporta geolocalização.');
      return;
    }

    setStatus('carregando');
    setMensagem('Obtendo sua localização...');
    setVagas([]);

    navigator.geolocation.getCurrentPosition(
      async (pos) => {
        try {
          const { latitude: lat, longitude: lng } = pos.coords;
          const dados = await vagasProximas({ lat, lng, raioKm });
          const filtradas = aplicarFiltros(dados);
          setVagas(filtradas);
          setStatus('ok');
          setMensagem(
            filtradas.length === 0
              ? 'Nenhuma vaga encontrada com esses filtros.'
              : `${filtradas.length} vaga(s) perto de você.`
          );
          setSelecionada({ lat, lng });
        } catch (erro) {
          setStatus('erro');
          setMensagem(erro.message || 'Erro ao buscar vagas próximas.');
        }
      },
      () => {
        setStatus('erro');
        setMensagem('Não conseguimos acessar sua localização.');
      },
      { enableHighAccuracy: true, timeout: 10000 }
    );
  }

  function selecionar(vaga) {
    setSelecionada({ lat: vaga.latitude, lng: vaga.longitude });
  }

  return (
    <section className="pagina">
      <div className="layout-filtros">
        <aside className="painel-filtros">
          <form onSubmit={buscarPorEndereco} className="busca">
            <div className="campo">
              <label htmlFor="endereco-filtro" className="somente-leitor">
                Endereço
              </label>
              <input
                id="endereco-filtro"
                className="campo__input"
                type="text"
                value={endereco}
                onChange={(e) => setEndereco(e.target.value)}
                placeholder="Endereço"
              />
            </div>
          </form>

          <FiltrosVaga
            filtros={filtros}
            onChange={setFiltros}
            raioKm={raioKm}
            onRaioChange={setRaioKm}
          />

          <div className="painel-filtros__acoes">
            <button
              className="botao botao--primario"
              onClick={buscarPorEndereco}
            >
              Pesquisar vagas
            </button>
            <button className="botao" onClick={buscarProximas}>
              📍 Próximas de mim
            </button>
          </div>
        </aside>

        <div className="painel-resultados">
          {mensagem && (
            <p
              className={`status ${
                status === 'erro' ? 'status--erro' : ''
              }`}
              role="status"
              aria-live="polite"
            >
              {mensagem}
            </p>
          )}

          <MapaLeaflet
            vagas={vagas}
            posicaoSelecionada={selecionada}
            centro={selecionada || undefined}
          />

          {vagas.length > 0 && (
            <ul className="lista">
              {vagas.map((vaga) => (
                <CardVaga
                  key={vaga.id}
                  vaga={vaga}
                  onSelecionar={selecionar}
                />
              ))}
            </ul>
          )}
        </div>
      </div>
    </section>
  );
}

export default function App() {
  const [abaAtiva, setAbaAtiva] = useState('buscar');

  return (
    <main className="hero">
      <header className="hero__topo">
        <h1 className="logo">ParkEasy</h1>
        <p className="slogan">Vagas por um preço bom perto de você</p>
      </header>

      <MenuAbas ativa={abaAtiva} onChange={setAbaAtiva} />

      <div className="aba-conteudo">
        {abaAtiva === 'buscar' && <BuscarVagas />}
        {abaAtiva === 'filtrar' && <FiltrarVagas />}
      </div>
    </main>
  );
}