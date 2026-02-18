package ru.hantu.ralp;

import net.minecraft.network.chat.Component;

public class Messages {
    public static Component get(String key) {
        String lang = ConfigManager.language;
        return Component.literal(switch (lang) {
            case "ru" -> getRu(key);
            case "de" -> getDe(key);
            case "es" -> getEs(key);
            case "fr" -> getFr(key);
            case "pt" -> getPt(key);
            case "tr" -> getTr(key);
            case "it" -> getIt(key);
            case "ko" -> getKo(key);
            case "ja" -> getJa(key);
            case "zh" -> getZh(key);
            case "pl" -> getPl(key);
            default -> getEn(key);
        });
    }

    // === ENGLISH ===
    private static String getEn(String key) {
        return switch (key) {
            case "register.usage" -> "§6§l[RaLP] §r§aUsage: §7/register <password> <repeat>";
            case "register.success" -> "§aSuccessfully registered and logged in!";
            case "register.already" -> "§cYou are already registered!";
            case "register.passwords-not-match" -> "§cPasswords do not match!";
            case "register.too-short" -> "§cPassword must be at least " + ConfigManager.minPasswordLength + " characters!";
            case "login.usage" -> "§6§l[RaLP] §r§cUsage: §7/login <password>";
            case "login.success" -> "§aSuccessfully logged in!";
            case "login.wrong" -> "§cWrong password!";
            case "login.not-registered" -> "§cYou need to register first!";
            case "login.blocked" -> "§cToo many failed attempts! You are blocked.";
            case "login.blocked-time" -> "§cYou are blocked for {time} seconds!";
            case "login.already" -> "§cYou are already logged in!";
            case "changepassword.success" -> "§aPassword changed! Please log in again.";
            case "changepassword.error" -> "§cFailed to change password!";
            case "unreg.success" -> "§aUnregistered player {player}!";
            case "unreg.not-registered" -> "§cPlayer is not registered!";
            case "errors.player-only" -> "§cThis command is for players only!";
            case "errors.not-logged-in" -> "§cYou must log in first!";
            case "errors.no-permission" -> "§cYou don't have permission!";
            case "help.message" -> "§6§l[RaLP Help]\n§r§7/ralp set max-attempts <N> §8- Max login attempts\n§7/ralp set block-time <sec> §8- Block duration\n§7/ralp set min-length <N> §8- Min password length\n§7/ralp set language <en|ru|de|es|fr|pt|tr|it|ko|ja|zh|pl> §8- Language\n§7/ralp reload §8- Reload config from file";
            default -> "§c[Unknown message: " + key + "]";
        };
    }

    // === RUSSIAN ===
    private static String getRu(String key) {
        return switch (key) {
            case "register.usage" -> "§6§l[RaLP] §r§aИспользуйте: §7/register <пароль> <повтор>";
            case "register.success" -> "§aВы успешно зарегистрированы и вошли в систему!";
            case "register.already" -> "§cВы уже зарегистрированы!";
            case "register.passwords-not-match" -> "§cПароли не совпадают!";
            case "register.too-short" -> "§cПароль должен быть не короче " + ConfigManager.minPasswordLength + " символов!";
            case "login.usage" -> "§6§l[RaLP] §r§cИспользуйте: §7/login <пароль>";
            case "login.success" -> "§aВы успешно вошли в систему!";
            case "login.wrong" -> "§cНеверный пароль!";
            case "login.not-registered" -> "§cСначала зарегистрируйтесь!";
            case "login.blocked" -> "§cСлишком много попыток! Вы заблокированы.";
            case "login.blocked-time" -> "§cВы заблокированы на {time} секунд!";
            case "login.already" -> "§cВы уже вошли в систему!";
            case "changepassword.success" -> "§aПароль изменён! Пожалуйста, войдите снова.";
            case "changepassword.error" -> "§cНе удалось изменить пароль!";
            case "unreg.success" -> "§aРегистрация игрока {player} удалена!";
            case "unreg.not-registered" -> "§cИгрок не зарегистрирован!";
            case "errors.player-only" -> "§cЭта команда только для игроков!";
            case "errors.not-logged-in" -> "§cВы должны сначала войти в аккаунт!";
            case "errors.no-permission" -> "§cУ вас нет прав!";
            case "help.message" -> "§6§l[RaLP Помощь]\n§r§7/ralp set max-attempts <N> §8- Макс. попыток входа\n§7/ralp set block-time <сек> §8- Время блокировки\n§7/ralp set min-length <N> §8- Мин. длина пароля\n§7/ralp set language <en|ru|de|es|fr|pt|tr|it|ko|ja|zh|pl> §8- Язык\n§7/ralp reload §8- Перезагрузить конфиг";
            default -> "§c[Неизвестное сообщение: " + key + "]";
        };
    }

    // === GERMAN ===
    private static String getDe(String key) {
        return switch (key) {
            case "register.usage" -> "§6§l[RaLP] §r§aVerwendung: §7/register <Passwort> <wiederholen>";
            case "register.success" -> "§aErfolgreich registriert und eingeloggt!";
            case "register.already" -> "§cSie sind bereits registriert!";
            case "register.passwords-not-match" -> "§cPasswörter stimmen nicht überein!";
            case "register.too-short" -> "§cPasswort muss mindestens " + ConfigManager.minPasswordLength + " Zeichen lang sein!";
            case "login.usage" -> "§6§l[RaLP] §r§cVerwendung: §7/login <Passwort>";
            case "login.success" -> "§aErfolgreich eingeloggt!";
            case "login.wrong" -> "§cFalsches Passwort!";
            case "login.not-registered" -> "§cSie müssen sich zuerst registrieren!";
            case "login.blocked" -> "§cZu viele fehlgeschlagene Versuche! Sie sind gesperrt.";
            case "login.blocked-time" -> "§cSie sind für {time} Sekunden gesperrt!";
            case "login.already" -> "§cSie sind bereits eingeloggt!";
            case "changepassword.success" -> "§aPasswort geändert! Bitte erneut einloggen.";
            case "changepassword.error" -> "§cPasswortänderung fehlgeschlagen!";
            case "unreg.success" -> "§aSpieler {player} wurde entfernt!";
            case "unreg.not-registered" -> "§cSpieler ist nicht registriert!";
            case "errors.player-only" -> "§cDieser Befehl ist nur für Spieler!";
            case "errors.not-logged-in" -> "§cSie müssen sich zuerst einloggen!";
            case "errors.no-permission" -> "§cSie haben keine Berechtigung!";
            case "help.message" -> "§6§l[RaLP Hilfe]\n§r§7/ralp set max-attempts <N> §8- Max. Login-Versuche\n§7/ralp set block-time <Sek> §8- Sperrdauer\n§7/ralp set min-length <N> §8- Min. Passwortlänge\n§7/ralp set language <en|ru|de|es|fr|pt|tr|it|ko|ja|zh|pl> §8- Sprache\n§7/ralp reload §8- Konfiguration neu laden";
            default -> "§c[Unbekannte Nachricht: " + key + "]";
        };
    }

    // === SPANISH ===
    private static String getEs(String key) {
        return switch (key) {
            case "register.usage" -> "§6§l[RaLP] §r§aUso: §7/register <contraseña> <repetir>";
            case "register.success" -> "§a¡Registrado e iniciado sesión correctamente!";
            case "register.already" -> "§c¡Ya estás registrado!";
            case "register.passwords-not-match" -> "§c¡Las contraseñas no coinciden!";
            case "register.too-short" -> "§c¡La contraseña debe tener al menos " + ConfigManager.minPasswordLength + " caracteres!";
            case "login.usage" -> "§6§l[RaLP] §r§cUso: §7/login <contraseña>";
            case "login.success" -> "§a¡Has iniciado sesión correctamente!";
            case "login.wrong" -> "§c¡Contraseña incorrecta!";
            case "login.not-registered" -> "§c¡Primero debes registrarte!";
            case "login.blocked" -> "§c¡Demasiados intentos fallidos! Estás bloqueado.";
            case "login.blocked-time" -> "§c¡Estás bloqueado por {time} segundos!";
            case "login.already" -> "§c¡Ya has iniciado sesión!";
            case "changepassword.success" -> "§a¡Contraseña cambiada! Por favor inicia sesión nuevamente.";
            case "changepassword.error" -> "§c¡Error al cambiar la contraseña!";
            case "unreg.success" -> "§a¡Jugador {player} eliminado!";
            case "unreg.not-registered" -> "§c¡El jugador no está registrado!";
            case "errors.player-only" -> "§c¡Este comando es solo para jugadores!";
            case "errors.not-logged-in" -> "§c¡Debes iniciar sesión primero!";
            case "errors.no-permission" -> "§c¡No tienes permiso!";
            case "help.message" -> "§6§l[Ayuda RaLP]\n§r§7/ralp set max-attempts <N> §8- Intentos máximos\n§7/ralp set block-time <seg> §8- Duración del bloqueo\n§7/ralp set min-length <N> §8- Longitud mínima contraseña\n§7/ralp set language <en|ru|de|es|fr|pt|tr|it|ko|ja|zh|pl> §8- Idioma\n§7/ralp reload §8- Recargar configuración";
            default -> "§c[Mensaje desconocido: " + key + "]";
        };
    }

    // === FRENCH ===
    private static String getFr(String key) {
        return switch (key) {
            case "register.usage" -> "§6§l[RaLP] §r§aUtilisation : §7/register <motdepasse> <confirmer>";
            case "register.success" -> "§aInscription et connexion réussies !";
            case "register.already" -> "§cVous êtes déjà inscrit !";
            case "register.passwords-not-match" -> "§cLes mots de passe ne correspondent pas !";
            case "register.too-short" -> "§cLe mot de passe doit contenir au moins " + ConfigManager.minPasswordLength + " caractères !";
            case "login.usage" -> "§6§l[RaLP] §r§cUtilisation : §7/login <motdepasse>";
            case "login.success" -> "§aConnexion réussie !";
            case "login.wrong" -> "§cMot de passe incorrect !";
            case "login.not-registered" -> "§cVous devez d'abord vous inscrire !";
            case "login.blocked" -> "§cTrop de tentatives échouées ! Vous êtes bloqué.";
            case "login.blocked-time" -> "§cVous êtes bloqué pendant {time} secondes !";
            case "login.already" -> "§cVous êtes déjà connecté !";
            case "changepassword.success" -> "§aMot de passe modifié ! Veuillez vous reconnecter.";
            case "changepassword.error" -> "§cÉchec de la modification du mot de passe !";
            case "unreg.success" -> "§aJoueur {player} supprimé !";
            case "unreg.not-registered" -> "§cLe joueur n'est pas inscrit !";
            case "errors.player-only" -> "§cCette commande est réservée aux joueurs !";
            case "errors.not-logged-in" -> "§cVous devez d'abord vous connecter !";
            case "errors.no-permission" -> "§cVous n'avez pas la permission !";
            case "help.message" -> "§6§l[Aide RaLP]\n§r§7/ralp set max-attempts <N> §8- Tentatives max\n§7/ralp set block-time <sec> §8- Durée du blocage\n§7/ralp set min-length <N> §8- Longueur min mot de passe\n§7/ralp set language <en|ru|de|es|fr|pt|tr|it|ko|ja|zh|pl> §8- Langue\n§7/ralp reload §8- Recharger la config";
            default -> "§c[Message inconnu : " + key + "]";
        };
    }

    // === PORTUGUESE ===
    private static String getPt(String key) {
        return switch (key) {
            case "register.usage" -> "§6§l[RaLP] §r§aUtilização: §7/register <palavra-passe> <confirmar>";
            case "register.success" -> "§aRegisto e login efetuados com sucesso!";
            case "register.already" -> "§cJá estás registado!";
            case "register.passwords-not-match" -> "§cAs palavras-passe não coincidem!";
            case "register.too-short" -> "§cA palavra-passe deve ter pelo menos " + ConfigManager.minPasswordLength + " caracteres!";
            case "login.usage" -> "§6§l[RaLP] §r§cUtilização: §7/login <palavra-passe>";
            case "login.success" -> "§aLogin efetuado com sucesso!";
            case "login.wrong" -> "§cPalavra-passe incorreta!";
            case "login.not-registered" -> "§cPrimeiro tens de te registar!";
            case "login.blocked" -> "§cDemasiadas tentativas falhadas! Estás bloqueado.";
            case "login.blocked-time" -> "§cEstás bloqueado por {time} segundos!";
            case "login.already" -> "§cJá fizeste login!";
            case "changepassword.success" -> "§aPalavra-passe alterada! Por favor faz login novamente.";
            case "changepassword.error" -> "§cFalha ao alterar a palavra-passe!";
            case "unreg.success" -> "§aJogador {player} removido!";
            case "unreg.not-registered" -> "§cO jogador não está registado!";
            case "errors.player-only" -> "§cEste comando só pode ser usado por jogadores!";
            case "errors.not-logged-in" -> "§cPrimeiro tens de fazer login!";
            case "errors.no-permission" -> "§cNão tens permissão!";
            case "help.message" -> "§6§l[Ajuda RaLP]\n§r§7/ralp set max-attempts <N> §8- Tentativas máximas\n§7/ralp set block-time <seg> §8- Duração do bloqueio\n§7/ralp set min-length <N> §8- Tamanho mínimo da palavra-passe\n§7/ralp set language <en|ru|de|es|fr|pt|tr|it|ko|ja|zh|pl> §8- Idioma\n§7/ralp reload §8- Recarregar configuração";
            default -> "§c[Mensagem desconhecida: " + key + "]";
        };
    }

    // === TURKISH ===
    private static String getTr(String key) {
        return switch (key) {
            case "register.usage" -> "§6§l[RaLP] §r§aKullanım: §7/register <şifre> <tekrar>";
            case "register.success" -> "§aBaşarıyla kaydoldunuz ve giriş yaptınız!";
            case "register.already" -> "§cZaten kayıtlısınız!";
            case "register.passwords-not-match" -> "§cŞifreler uyuşmuyor!";
            case "register.too-short" -> "§cŞifre en az " + ConfigManager.minPasswordLength + " karakter olmalıdır!";
            case "login.usage" -> "§6§l[RaLP] §r§cKullanım: §7/login <şifre>";
            case "login.success" -> "§aBaşarıyla giriş yaptınız!";
            case "login.wrong" -> "§cYanlış şifre!";
            case "login.not-registered" -> "§cÖnce kayıt olmalısınız!";
            case "login.blocked" -> "§cÇok fazla başarısız deneme! Engellendiniz.";
            case "login.blocked-time" -> "§c{time} saniye boyunca engellendiniz!";
            case "login.already" -> "§cZaten giriş yaptınız!";
            case "changepassword.success" -> "§aŞifre değiştirildi! Lütfen tekrar giriş yapın.";
            case "changepassword.error" -> "§cŞifre değiştirilemedi!";
            case "unreg.success" -> "§aOyuncu {player} kaldırıldı!";
            case "unreg.not-registered" -> "§cOyuncu kayıtlı değil!";
            case "errors.player-only" -> "§cBu komut sadece oyuncular içindir!";
            case "errors.not-logged-in" -> "§cÖnce giriş yapmalısınız!";
            case "errors.no-permission" -> "§cİzniniz yok!";
            case "help.message" -> "§6§l[RaLP Yardım]\n§r§7/ralp set max-attempts <N> §8- Maks. giriş denemesi\n§7/ralp set block-time <sn> §8- Engelleme süresi\n§7/ralp set min-length <N> §8- Min. şifre uzunluğu\n§7/ralp set language <en|ru|de|es|fr|pt|tr|it|ko|ja|zh|pl> §8- Dil\n§7/ralp reload §8- Yapılandırmayı yeniden yükle";
            default -> "§c[Bilinmeyen mesaj: " + key + "]";
        };
    }

    // === ITALIAN ===
    private static String getIt(String key) {
        return switch (key) {
            case "register.usage" -> "§6§l[RaLP] §r§aUso: §7/register <password> <ripeti>";
            case "register.success" -> "§aRegistrazione e login effettuati con successo!";
            case "register.already" -> "§cSei già registrato!";
            case "register.passwords-not-match" -> "§cLe password non corrispondono!";
            case "register.too-short" -> "§cLa password deve essere di almeno " + ConfigManager.minPasswordLength + " caratteri!";
            case "login.usage" -> "§6§l[RaLP] §r§cUso: §7/login <password>";
            case "login.success" -> "§aLogin effettuato con successo!";
            case "login.wrong" -> "§cPassword errata!";
            case "login.not-registered" -> "§cDevi prima registrarti!";
            case "login.blocked" -> "§cTroppi tentativi falliti! Sei bloccato.";
            case "login.blocked-time" -> "§cSei bloccato per {time} secondi!";
            case "login.already" -> "§cHai già effettuato il login!";
            case "changepassword.success" -> "§aPassword cambiata! Per favore effettua di nuovo il login.";
            case "changepassword.error" -> "§cErrore nel cambio password!";
            case "unreg.success" -> "§aGiocatore {player} rimosso!";
            case "unreg.not-registered" -> "§cIl giocatore non è registrato!";
            case "errors.player-only" -> "§cQuesto comando è solo per i giocatori!";
            case "errors.not-logged-in" -> "§cDevi prima effettuare il login!";
            case "errors.no-permission" -> "§cNon hai il permesso!";
            case "help.message" -> "§6§l[Aiuto RaLP]\n§r§7/ralp set max-attempts <N> §8- Tentativi massimi\n§7/ralp set block-time <sec> §8- Durata blocco\n§7/ralp set min-length <N> §8- Lunghezza min password\n§7/ralp set language <en|ru|de|es|fr|pt|tr|it|ko|ja|zh|pl> §8- Lingua\n§7/ralp reload §8- Ricarica configurazione";
            default -> "§c[Messaggio sconosciuto: " + key + "]";
        };
    }

    // === KOREAN ===
    private static String getKo(String key) {
        return switch (key) {
            case "register.usage" -> "§6§l[RaLP] §r§a사용법: §7/register <비밀번호> <확인>";
            case "register.success" -> "§a성공적으로 등록하고 로그인했습니다!";
            case "register.already" -> "§c이미 등록되어 있습니다!";
            case "register.passwords-not-match" -> "§c비밀번호가 일치하지 않습니다!";
            case "register.too-short" -> "§c비밀번호는 최소 " + ConfigManager.minPasswordLength + "자 이상이어야 합니다!";
            case "login.usage" -> "§6§l[RaLP] §r§c사용법: §7/login <비밀번호>";
            case "login.success" -> "§a성공적으로 로그인했습니다!";
            case "login.wrong" -> "§c잘못된 비밀번호입니다!";
            case "login.not-registered" -> "§c먼저 등록해 주세요!";
            case "login.blocked" -> "§c실패한 시도가 너무 많습니다! 차단되었습니다.";
            case "login.blocked-time" -> "§c{time}초 동안 차단되었습니다!";
            case "login.already" -> "§c이미 로그인했습니다!";
            case "changepassword.success" -> "§a비밀번호를 변경했습니다! 다시 로그인해 주세요.";
            case "changepassword.error" -> "§c비밀번호 변경에 실패했습니다!";
            case "unreg.success" -> "§a플레이어 {player} 님의 계정이 삭제되었습니다!";
            case "unreg.not-registered" -> "§c플레이어가 등록되지 않았습니다!";
            case "errors.player-only" -> "§c이 명령어는 플레이어만 사용할 수 있습니다!";
            case "errors.not-logged-in" -> "§c먼저 로그인해 주세요!";
            case "errors.no-permission" -> "§c권한이 없습니다!";
            case "help.message" -> "§6§l[RaLP 도움말]\n§r§7/ralp set max-attempts <N> §8- 최대 로그인 시도\n§7/ralp set block-time <초> §8- 차단 지속 시간\n§7/ralp set min-length <N> §8- 최소 비밀번호 길이\n§7/ralp set language <en|ru|de|es|fr|pt|tr|it|ko|ja|zh|pl> §8- 언어\n§7/ralp reload §8- 설정 다시 불러오기";
            default -> "§c[알 수 없는 메시지: " + key + "]";
        };
    }

    // === JAPANESE ===
    private static String getJa(String key) {
        return switch (key) {
            case "register.usage" -> "§6§l[RaLP] §r§a使い方: §7/register <パスワード> <確認>";
            case "register.success" -> "§a登録とログインに成功しました！";
            case "register.already" -> "§c既に登録されています！";
            case "register.passwords-not-match" -> "§cパスワードが一致しません！";
            case "register.too-short" -> "§cパスワードは少なくとも" + ConfigManager.minPasswordLength + "文字以上で入力してください！";
            case "login.usage" -> "§6§l[RaLP] §r§c使い方: §7/login <パスワード>";
            case "login.success" -> "§aログインに成功しました！";
            case "login.wrong" -> "§cパスワードが間違っています！";
            case "login.not-registered" -> "§cまず登録してください！";
            case "login.blocked" -> "§c失敗した試行が多すぎます！ブロックされました。";
            case "login.blocked-time" -> "§c{time}秒間ブロックされました！";
            case "login.already" -> "§cすでにログインしています！";
            case "changepassword.success" -> "§aパスワードを変更しました！再度ログインしてください。";
            case "changepassword.error" -> "§cパスワードの変更に失敗しました！";
            case "unreg.success" -> "§aプレイヤー {player} のアカウントを削除しました！";
            case "unreg.not-registered" -> "§cプレイヤーは登録されていません！";
            case "errors.player-only" -> "§cこのコマンドはプレイヤーのみ使用できます！";
            case "errors.not-logged-in" -> "§cまずログインしてください！";
            case "errors.no-permission" -> "§c権限がありません！";
            case "help.message" -> "§6§l[RaLP ヘルプ]\n§r§7/ralp set max-attempts <N> §8- 最大ログイン試行回数\n§7/ralp set block-time <秒> §8- ブロック期間\n§7/ralp set min-length <N> §8- 最小パスワード長\n§7/ralp set language <en|ru|de|es|fr|pt|tr|it|ko|ja|zh|pl> §8- 言語\n§7/ralp reload §8- 設定を再読み込み";
            default -> "§c[不明なメッセージ: " + key + "]";
        };
    }

    // === CHINESE ===
    private static String getZh(String key) {
        return switch (key) {
            case "register.usage" -> "§6§l[RaLP] §r§a用法: §7/register <密码> <重复>";
            case "register.success" -> "§a您已成功注册并登录！";
            case "register.already" -> "§c您已经注册过了！";
            case "register.passwords-not-match" -> "§c密码不匹配！";
            case "register.too-short" -> "§c密码长度至少为 " + ConfigManager.minPasswordLength + " 个字符！";
            case "login.usage" -> "§6§l[RaLP] §r§c用法: §7/login <密码>";
            case "login.success" -> "§a登录成功！";
            case "login.wrong" -> "§c密码错误！";
            case "login.not-registered" -> "§c请先注册！";
            case "login.blocked" -> "§c失败尝试次数过多！您已被封禁。";
            case "login.blocked-time" -> "§c您已被封禁 {time} 秒！";
            case "login.already" -> "§c您已经登录了！";
            case "changepassword.success" -> "§a密码修改成功！请重新登录。";
            case "changepassword.error" -> "§c密码修改失败！";
            case "unreg.success" -> "§a玩家 {player} 的账户已删除！";
            case "unreg.not-registered" -> "§c玩家未注册！";
            case "errors.player-only" -> "§c该命令只能由玩家执行！";
            case "errors.not-logged-in" -> "§c请先登录！";
            case "errors.no-permission" -> "§c您没有权限！";
            case "help.message" -> "§6§l[RaLP 帮助]\n§r§7/ralp set max-attempts <N> §8- 最大登录尝试次数\n§7/ralp set block-time <秒> §8- 封禁时长\n§7/ralp set min-length <N> §8- 最小密码长度\n§7/ralp set language <en|ru|de|es|fr|pt|tr|it|ko|ja|zh|pl> §8- 语言\n§7/ralp reload §8- 重新加载配置";
            default -> "§c[未知消息: " + key + "]";
        };
    }

    // === POLISH ===
    private static String getPl(String key) {
        return switch (key) {
            case "register.usage" -> "§6§l[RaLP] §r§aUżycie: §7/register <hasło> <powtórz>";
            case "register.success" -> "§aPomyślnie zarejestrowano i zalogowano!";
            case "register.already" -> "§cJesteś już zarejestrowany!";
            case "register.passwords-not-match" -> "§cHasła nie pasują do siebie!";
            case "register.too-short" -> "§cHasło musi mieć co najmniej " + ConfigManager.minPasswordLength + " znaków!";
            case "login.usage" -> "§6§l[RaLP] §r§cUżycie: §7/login <hasło>";
            case "login.success" -> "§aPomyślnie zalogowano!";
            case "login.wrong" -> "§cNieprawidłowe hasło!";
            case "login.not-registered" -> "§cNajpierw się zarejestruj!";
            case "login.blocked" -> "§cZbyt wiele nieudanych prób! Zostałeś zablokowany.";
            case "login.blocked-time" -> "§cZostałeś zablokowany na {time} sekund!";
            case "login.already" -> "§cJesteś już zalogowany!";
            case "changepassword.success" -> "§aHasło zostało zmienione! Zaloguj się ponownie.";
            case "changepassword.error" -> "§cNie udało się zmienić hasła!";
            case "unreg.success" -> "§aKonto gracza {player} zostało usunięte!";
            case "unreg.not-registered" -> "§cGracz nie jest zarejestrowany!";
            case "errors.player-only" -> "§cTa komenda jest tylko dla graczy!";
            case "errors.not-logged-in" -> "§cNajpierw się zaloguj!";
            case "errors.no-permission" -> "§cNie masz uprawnień!";
            case "help.message" -> "§6§l[Pomoc RaLP]\n§r§7/ralp set max-attempts <N> §8- Maks. prób logowania\n§7/ralp set block-time <sek> §8- Czas blokady\n§7/ralp set min-length <N> §8- Min. długość hasła\n§7/ralp set language <en|ru|de|es|fr|pt|tr|it|ko|ja|zh|pl> §8- Język\n§7/ralp reload §8- Przeładuj konfigurację";
            default -> "§c[Nieznana wiadomość: " + key + "]";
        };
    }
}