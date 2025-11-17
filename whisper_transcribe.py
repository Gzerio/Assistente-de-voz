import sys
import os
import whisper

def log(msg: str):
    """Loga no stderr pra não misturar com a transcrição."""
    print(msg, file=sys.stderr, flush=True)

def main():

    if len(sys.argv) < 2:
        log("[WhisperOnce] Nenhum arquivo de áudio informado.")
        print("")  
        sys.exit(1)

    audio_path = sys.argv[1]
    model_name = sys.argv[2] if len(sys.argv) >= 3 else "small"
    language = sys.argv[3] if len(sys.argv) >= 4 else "pt"

    if not os.path.isfile(audio_path):
        log(f"[WhisperOnce] Arquivo não encontrado: {audio_path}")
        print("") 
        sys.exit(1)

    log(f"[WhisperOnce] Modelo='{model_name}', idioma='{language}'")
    log(f"[WhisperOnce] Carregando modelo...")

    try:
        model = whisper.load_model(model_name)
    except Exception as e:
        log(f"[WhisperOnce] ERRO ao carregar modelo: {e}")
        print("")
        sys.exit(1)

    log(f"[WhisperOnce] Transcrevendo: {audio_path}")

    try:
        result = model.transcribe(
            audio_path,
            language=language,
            fp16=False,
            temperature=0.0,
            best_of=3,
            beam_size=3,
            condition_on_previous_text=False,
            without_timestamps=True,
        )
        text = result.get("text", "") or ""
        text = text.strip()
    except Exception as e:
        log(f"[WhisperOnce] ERRO ao transcrever: {e}")
        text = ""

    print(text)

if __name__ == "__main__":
    main()
