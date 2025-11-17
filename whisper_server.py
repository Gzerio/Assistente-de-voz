import sys
import os
import whisper

def log(msg: str):
    """Loga no stderr pra não misturar com a saída de texto do reconhecimento."""
    print(msg, file=sys.stderr, flush=True)

def main():

    model_name = sys.argv[1] if len(sys.argv) >= 2 else "small"
    language = sys.argv[2] if len(sys.argv) >= 3 else "pt"

    log(f"[WhisperServer] Iniciando com modelo='{model_name}', idioma='{language}'")

    try:
        log(f"[WhisperServer] Carregando modelo: {model_name} ...")
        model = whisper.load_model(model_name)
        log("[WhisperServer] Modelo carregado com sucesso.")
    except Exception as e:
        log(f"[WhisperServer] ERRO ao carregar modelo: {e}")
        sys.exit(1)

    def transcribe(path: str) -> str:
        if not os.path.isfile(path):
            log(f"[WhisperServer] Arquivo não encontrado: {path}")
            return ""
        try:
            result = model.transcribe(
                path,
                language=language,
                fp16=False,
                temperature=0.0,
                best_of=3,
                beam_size=3,
                condition_on_previous_text=False,
                without_timestamps=True,
            )
            text = result.get("text", "") or ""
            return text.strip()
        except Exception as e:
            log(f"[WhisperServer] ERRO ao transcrever '{path}': {e}")
            return ""

    log("[WhisperServer] Pronto. Aguardando caminhos de arquivos via STDIN...")


    for line in sys.stdin:
        line = line.strip()
        if not line:
            continue

        if line == "EXIT":
            log("[WhisperServer] Comando EXIT recebido. Encerrando.")
            break

        log(f"[WhisperServer] Recebido para transcrever: {line}")

        text = transcribe(line)


        print(text, flush=True)

    log("[WhisperServer] Finalizado.")

if __name__ == "__main__":
    main()
