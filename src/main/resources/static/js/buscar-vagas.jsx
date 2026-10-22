const { useState, useRef, useEffect, useCallback } = React;

const USE_MOCK = true;
const API_URL = '/api/vagas';

const VAGAS_MOCK = [
  { id: 1, nome: 'Estacionamento Central', endereco: 'Rua da Aurora, 120', distanciaMetros: 250, precoHora: 8.5, vagasLivres: 12, acessivel: true },
  { id: 2, nome: 'Park Shopping', endereco: 'Av. Conde da Boa Vista, 900', distanciaMetros: 480, precoHora: 4.5, vagasLivres: 4, acessivel: false },
  { id: 3, nome: 'Garagem do Bairro', endereco: 'Rua do Hospício, 45', distanciaMetros: 900, precoHora: 6, vagasLivres: 20, acessivel: true },
  { id: 4, nome: 'Vaga Segura 24h', endereco: 'Rua Gervásio Pires, 300', distanciaMetros: 1300, precoHora: 7.5, vagasLivres: 2, acessivel: false },
]; //Valor das vagas estimados e quantas vagas disponiveis existem

async function buscarVagas(params, signal) {
  if (USE_MOCK) {
    await new Promise((resolve) => setTimeout(resolve, 400));
    return params.acessivel ? VAGAS_MOCK.filter((v) => v.acessivel) : VAGAS_MOCK;
  }
  const query = new URLSearchParams(params).toString();
  const resposta = await fetch(`${API_URL}?${query}`, { signal });
  if (!resposta.ok) throw new Error('Falha ao buscar vagas');
  return resposta.json();
}

/* ---------------- Formatação ---------------- */
const formatarPreco = (valor) =>
  valor.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });

const formatarDistancia = (metros) =>
  metros < 1000
    ? `${metros} m`
    : `${(metros / 1000).toFixed(1).replace('.', ',')} km`;

/* ---------------- Componentes ---------------- */
function CardVaga({ vaga }) {
  const classeLivres =
    vaga.vagasLivres <= 3
      ? 'vaga__livres--poucas'
      : vaga.vagasLivres >= 10
      ? 'vaga__livres--muitas'
      : '';

  return (
    <li className="vaga">
      <h2 className="vaga__nome">
        {vaga.nome}
        {vaga.acessivel && <span title="Vaga acessível (PCD)" aria-label="Vaga acessível"> ♿</span>}
      </h2>
      <p className="vaga__endereco">{vaga.endereco}</p>
      <div className="vaga__rodape">
        <span className="vaga__preco">{formatarPreco(vaga.precoHora)}/h</span>
        <span>
          {formatarDistancia(vaga.distanciaMetros)} ·{' '}
          <span className={classeLivres}>{vaga.vagasLivres} livres</span>
        </span>
      </div>
    </li>
  );
}

function BuscaVagas() {
  const [local, setLocal] = useState('');
  const [vagas, setVagas] = useState([]);
  const [status, setStatus] = useState('parado'); // parado | carregando | ok | erro
  const [mensagem, setMensagem] = useState('');
  const [tempoReal, setTempoReal] = useState(false);
  const [somenteAcessiveis, setSomenteAcessiveis] = useState(false);

  const watchId = useRef(null);
  const abortRef = useRef(null);
  const ultimaPos = useRef(null);
  const acessivelRef = useRef(false);

  // Mantém o valor atual do filtro acessível dentro do watchPosition
  useEffect(() => {
    acessivelRef.current = somenteAcessiveis;
  }, [somenteAcessiveis]);

  // Cleanup: cancela requisição pendente e para o watch ao desmontar
  useEffect(() => {
    return () => {
      if (watchId.current !== null) {
        navigator.geolocation.clearWatch(watchId.current);
      }
      abortRef.current?.abort();
    };
  }, []);

  const carregar = useCallback(async (params) => {
    abortRef.current?.abort();
    const controller = new AbortController();
    abortRef.current = controller;

    setStatus('carregando');
    setMensagem('Buscando vagas...');

    try {
      const dados = await buscarVagas(params, controller.signal);
      if (controller.signal.aborted) return;
      setVagas(dados);
      setStatus('ok');
      setMensagem(
        dados.length === 0
          ? params.acessivel
            ? 'Nenhuma vaga acessível encontrada por aqui.'
            : 'Nenhuma vaga encontrada por aqui.'
          : ''
      );
    } catch (erro) {
      if (erro.name === 'AbortError') return;
      setStatus('erro');
      setMensagem('Não foi possível buscar as vagas. Tente novamente.');
    }
  }, []);

  function aoEnviar(evento) {
    evento.preventDefault();
    if (!local.trim()) {
      setStatus('erro');
      setMensagem('Digite sua localização para encontrar vagas próximas.');
      return;
    }
    carregar({ local: local.trim(), acessivel: somenteAcessiveis });
  }

  function alternarTempoReal() {
    // Desativar
    if (tempoReal) {
      if (watchId.current !== null) {
        navigator.geolocation.clearWatch(watchId.current);
        watchId.current = null;
      }
      ultimaPos.current = null;
      setTempoReal(false);
      setMensagem('');
      return;
    }

    // Ativar
    if (!('geolocation' in navigator)) {
      setStatus('erro');
      setMensagem('Seu navegador não permite usar a localização.');
      return;
    }

    setTempoReal(true);
    setMensagem('Obtendo sua localização...');
    setStatus('carregando');

    watchId.current = navigator.geolocation.watchPosition(
      (posicao) => {
        const { latitude: lat, longitude: lng } = posicao.coords;

        // Ignora variações menores que ~50m
        const anterior = ultimaPos.current;
        if (anterior) {
          const dLat = Math.abs(anterior.lat - lat);
          const dLng = Math.abs(anterior.lng - lng);
          if (dLat < 0.0005 && dLng < 0.0005) return;
        }
        ultimaPos.current = { lat, lng };

        carregar({ lat, lng, acessivel: acessivelRef.current });
      },
      () => {
        if (watchId.current !== null) {
          navigator.geolocation.clearWatch(watchId.current);
          watchId.current = null;
        }
        setTempoReal(false);
        setStatus('erro');
        setMensagem('Não conseguimos acessar sua localização. Verifique a permissão do navegador.');
      },
      { enableHighAccuracy: true, maximumAge: 10000 }
    );
  }

  return (
    <section aria-label="Busca de vagas">
      <form onSubmit={aoEnviar}>
        <div className="campo">
          <label htmlFor="local" className="somente-leitor">
            Sua localização atual
          </label>
          <input
            id="local"
            className="campo__input"
            type="text"
            value={local}
            onChange={(e) => setLocal(e.target.value)}
            placeholder="Digite sua localização atual para encontrar vagas próximas"
            autoComplete="street-address"
          />
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
        </div>

        <label className="filtro" style={{ display: 'block', margin: '0.5rem 0' }}>
          <input
            type="checkbox"
            checked={somenteAcessiveis}
            onChange={(e) => setSomenteAcessiveis(e.target.checked)}
          />{' '}
          Somente vagas acessíveis (PCD)
        </label>

        <div className="acoes">
          <button type="submit" className="botao">Encontrar vagas</button>
          <button
            type="button"
            className={`botao ${tempoReal ? 'botao--ativo' : ''}`}
            onClick={alternarTempoReal}
            aria-pressed={tempoReal}
          >
            {tempoReal
              ? 'Desativar localização em tempo real'
              : 'Ativar localização em tempo real'}
          </button>
          <a href="/vagas/pesquisar-endereco" className="botao" style={{ textDecoration: 'none' }}>Pesquisar Vagas por Endereço</a>
        </div>
      </form>

      <p
        className={`status ${status === 'erro' ? 'status--erro' : ''}`}
        role="status"
        aria-live="polite"
      >
        {mensagem}
      </p>

      {vagas.length > 0 && (
        <ul className="lista">
          {vagas.map((vaga) => (
            <CardVaga key={vaga.id} vaga={vaga} />
          ))}
        </ul>
      )}
    </section>
  );
}

ReactDOM.createRoot(document.getElementById('busca-vagas')).render(<BuscaVagas />);