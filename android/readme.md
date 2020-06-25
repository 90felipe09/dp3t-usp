# Android DP3T

## Setup

Certifique-se de que você está com o android studio instalado. Mesmo que você não irá desenvolver nele, a sua instalação é necessário para usufruir da compilação do projeto, geração do apk e testar em um emulador ou no seu android em mãos.

[Link para download do Android Studio](https://developer.android.com/studio)

Antecipo que é possível passar por estes processos só por linha de comando. Coloco essas instruções, pois eu sei como pode ser desgastante desenvolver usando o android studio como IDE. Assim, fica a liberdade de se utilizar o que você quiser como IDE.

### Criando uma dispositivo virtual

Dentro do Android studio procure pela opção:

- Tools > AVD Manager ou Clicar num ícone de um smartphone com um androidzinho na frente
- Selecione o botão create virtual device


### Adicionando variáveis de ambiente

Os caminhos que estou usando são onde o android são instalados. Procure pelo caminho onde você instalou.

Você saberá que deu certo caso após inserir este comando:

``` emulator -list-avds ```

Apareça a lista de emuladores que você pode utilizar.

A seguir tem duas instruções: por cmd ou por bash.

Não se esqueça de substituir a palavra User pelo nome do seu usuário no computador

#### CMD

PATH = %PATH%;C:\Users\User\AppData\Local\Android\Sdk\build-tools

PATH = %PATH%;C:\Users\User\AppData\Local\Android\Sdk\platform-tools

PATH = %PATH%;C:\Users\User\AppData\Local\Android\Sdk\emulator

PATH = %PATH%;C:\Users\User\AppData\Local\Android\Sdk\tools\bin

#### bash

- Ver as variáveis de ambiente setadas:
 - echo $PATH

- export PATH=$PATH:~/AppData/Local/Android/Sdk/build-tools

- export PATH=$PATH:~/AppData/Local/Android/Sdk/platform-tools

- export PATH=$PATH:~/AppData/Local/Android/Sdk/emulator

- export PATH=$PATH:~/AppData/Local/Android/Sdk/tools/bin

## Desenvolvendo

Desenvolver para android é saber da interação java com xml.

Os arquivos XML do projeto definem o fron end das telas e estão armazenadas em /res/layout.

Os arquivos de lógica, isto é, java estão em /java

## Buildando

Certifique-se de que seu working directory seja dentro da pasta android.

Isto porquê a ferramenta de build utilizada é o gradlew.

Com este comando é possível  identificar todos os comandos de gradlew disponíveis.

``` ./gradlew tasks ```

### Opções de build mais úteis

#### Emitir APK de depuração

``` ./gradlew assembleDebug ```

Irá cuspir um apk de depuração na pasta /build

#### Instalar no emulador funcionando

``` ./gradlew installDebug ```

Irá buildar e instalar no emulador que está ativo

#### Buildar versão final

Recomendo usar o próprio Android Studio. Fazer sem ele precisa gerar chave privada e uns rolê. Não compensa.

## Testando

Para testar o app é possível fazê-lo com um emulador de smartphone ou carregá-lo para o seu celular.

### Testando com o emulador

Primeiro será necessário iniciar o emulador. Para isso, verifique os emuladores disponíveis com:

``` emulator -list-avds ```

Em seguida, escolha com:

``` emulator -avd nome-do-dipositivo ```

Isso irá ligar um emulador na sua tela. 

``` ./gradlew installDebug ```

Irá buildar e instalar no emulador que está ativo

Ou para testar um apk buildado:

``` adb install  ./app/build/outputs/apk/app.apk ```

Isso irá pegar o apk gerado em build e instalá-lo no emulador.

adb é abstração que interage com o emulador.

Ao final, é só navegar pelo seu emulador que você vai achar ele.


