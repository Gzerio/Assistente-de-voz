# 🎙️ Assistente de Voz (Java + JavaFX)

Assistente de voz para desktop (Windows) feito em **Java + JavaFX**, com reconhecimento de fala usando **Whisper** via Python e execução automática de comandos no sistema.

> Automatize tarefas do seu PC só falando: abrir sites, pastas, aplicativos, ver hora/data e muito mais.

---

## 🖼️ Preview

<!-- TODO: exportar um print da tela principal e salvar em /docs ou /assets do repo -->
![Tela principal do Assistente de Voz](https://github.com/Gzerio/Assistente-de-voz/blob/af428c7ea4265466446399d8295bf092a119a12a/ADV.PNG)

---

## ✨ Funcionalidades

- 🎧 **Reconhecimento de voz** usando Whisper (via scripts Python)
  - Suporte a **Português (BR)** e **Inglês**
  - Modo servidor “always-on” ou modo “liga só quando gravar”
- 🖥️ **Interface moderna em JavaFX**
  - Tema escuro
  - Botão de microfone com animações de pulso
  - Visualizador de ondas de áudio
  - Campo de **“Comando reconhecido”** para mostrar o que foi entendido
- ⚙️ **Comandos nativos pré-configurados** (PT + EN)
  - Abrir Google, YouTube, Gmail, WhatsApp Web, Instagram, Facebook, TikTok, Spotify, Netflix…
  - Abrir pastas: Downloads, Documentos, Imagens, Música, Desktop
  - Abrir Bloco de Notas, Calculadora, Painel de Controle, Configurações do Windows
  - Dizer **hora atual** e **data de hoje** no console
- 🧩 **Comandos personalizados**
  - Janela de FAQ/Comandos permite cadastrar comandos extras
  - Frases de ativação e URL configuráveis
  - Salvos em `custom_commands.json`
- 🛠️ **Tela de Configurações**
  - Escolher modelo do Whisper: **small** ou **medium**
  - Idioma da transcrição: **pt** ou **en**
  - Ativar/desativar servidor sempre ligado
- 📦 **Instalador `.exe` via jpackage**
  - Pode ser instalado como app normal no Windows
  - Cria atalho no menu iniciar e na área de trabalho (opcional)

---

## 🏗️ Tecnologias utilizadas

- **Java 21**
- **JavaFX** (controls, fxml)
- **Gradle** (subprojeto `app`)
- **Python 3** + Whisper (via scripts `whisper_server.py` e `whisper_transcribe.py`)
- Animações e UI customizada em **JavaFX**  
- Empacotamento com **jpackage** (instalador `.exe` para Windows)

---

## 🚀 Como rodar em modo desenvolvimento

### 1. Requisitos

- **Java 21+** instalado
- **Gradle wrapper** (já vem no projeto)
- **Python 3** instalado
- Whisper configurado (modelo `small` ou `medium` configurado nos scripts Python)

### 2. Clonar o repositório

```bash
git clone https://github.com/Gzerio/Assistente-de-voz.git
cd Assistente-de-voz
