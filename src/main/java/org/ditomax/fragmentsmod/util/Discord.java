package org.ditomax.fragmentsmod.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import org.ditomax.fragmentsmod.Fragmentsmod;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Objects;


public class Discord {

    public static void sendDiscordMessageWithImage(String webhookUrl, ItemStack stack, PlayerEntity player) {
        new Thread(() -> {
            try {
                URI uri = URI.create(webhookUrl);
                HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
                conn.setRequestMethod("POST");

                // Boundary für Multipart
                String boundary = "----WebKitFormBoundary" + System.currentTimeMillis();
                conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                conn.setDoOutput(true);

                try (OutputStream os = conn.getOutputStream();
                     PrintWriter writer = new PrintWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8))) {

                    // JSON Payload Teil
                    writer.append("--").append(boundary).append("\r\n");
                    writer.append("Content-Disposition: form-data; name=\"payload_json\"").append("\r\n");
                    writer.append("Content-Type: application/json").append("\r\n");
                    writer.append("\r\n");

                    String playerName = player != null ? player.getName().getString() : "Unbekannter Spieler";
                    String itemName = stack.getName().getString();

                    // Spieler-Head URL (externe API)
                    String playerHeadUrl = "https://mc-heads.net/avatar/" + playerName + "/64";

                    String jsonPayload = "{"
                            + "\"content\":\"\","
                            + "\"embeds\":[{"
                            + "\"author\":{"
                            + "\"name\":\"" + playerName + "\","
                            + "\"icon_url\":\"" + playerHeadUrl + "\""
                            + "},"
                            + "\"title\":\"" + playerName + " hat " + itemName + " eingesammelt!\","
                            + "\"description\":\" \","
                            + "\"color\":16711680,"  // Rot
                            + "\"fields\":["
                            + "{\"name\":\"👤 Spieler\",\"value\":\"" + playerName + "\",\"inline\":true},"
                            + "{\"name\":\"🗡️ Item\",\"value\":\"" + itemName + "\",\"inline\":true}"
                            + "],"
                            + "\"image\":{\"url\":\"attachment://item_icon.png\"},"
                            + "\"thumbnail\":{\"url\":\"" + playerHeadUrl + "\"},"
                            + "\"timestamp\":\"" + java.time.Instant.now() + "\""
                            + "}]"
                            + "}";

                    writer.append(jsonPayload).append("\r\n");
                    writer.flush();

                    // Nur Item-Bild als Attachment (das Wichtige)
                    writer.append("--").append(boundary).append("\r\n");
                    writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"item_icon.png\"").append("\r\n");
                    writer.append("Content-Type: image/png").append("\r\n");
                    writer.append("\r\n");
                    writer.flush();

                    byte[] itemImageData = getItemTextureBytes(stack);
                    os.write(Objects.requireNonNullElseGet(itemImageData, Discord::getDefaultItemImageBytes));

                    os.flush();
                    writer.append("\r\n");
                    writer.append("--").append(boundary).append("--\r\n");
                    writer.flush();
                }

                // Response prüfen
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_NO_CONTENT || responseCode == HttpURLConnection.HTTP_OK) {
                    System.out.println("Discord-Nachricht erfolgreich gesendet!");
                } else {
                    System.err.println("Fehler beim Senden. Response Code: " + responseCode);
                    // Response-Body für Debug
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            System.err.println(line);
                        }
                    }
                }

                conn.disconnect();

            } catch (Exception e) {
                Fragmentsmod.LOGGER.warn("Keine gültige Webhook Adresse gefunden!");
            }
        }).start();
    }

    // Item-Textur laden (bestehende Methode)
    private static byte[] getItemTextureBytes(ItemStack stack) {
        try {
            String registryName = Registries.ITEM.getId(stack.getItem()).getPath();
            String texturePath = "/assets/fragmentsmod/textures/item/" + registryName + ".png";

            System.out.println("Suche Item-Textur: " + texturePath);

            InputStream stream = Fragmentsmod.class.getResourceAsStream(texturePath);

            if (stream == null) {
                String[] alternativeNames = {
                        registryName.toLowerCase(),
                        stack.getItem().getClass().getSimpleName().toLowerCase().replace("item", ""),
                        "schattenschleier",
                        "bogen_fragment"
                };

                for (String altName : alternativeNames) {
                    texturePath = "/assets/fragmentsmod/textures/item/" + altName + ".png";
                    stream = Fragmentsmod.class.getResourceAsStream(texturePath);
                    if (stream != null) {
                        System.out.println("Item-Textur gefunden: " + texturePath);
                        break;
                    }
                }
            }

            if (stream != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = stream.read(buffer)) != -1) {
                    baos.write(buffer, 0, bytesRead);
                }
                stream.close();
                return baos.toByteArray();
            }

        } catch (Exception e) {
            System.err.println("Fehler beim Laden der Item-Textur: " + e.getMessage());
        }
        return null;
    }

    // Fallback Item-Bild
    private static byte[] getDefaultItemImageBytes() {
        return new byte[]{
                (byte)0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A, 0x00, 0x00, 0x00, 0x0D, 0x49, 0x48, 0x44, 0x52,
                0x00, 0x00, 0x00, 0x10, 0x00, 0x00, 0x00, 0x10, 0x08, 0x06, 0x00, 0x00, 0x00, 0x1F, (byte)0xF3, (byte)0xFF,
                0x61, 0x00, 0x00, 0x00, 0x0B, 0x49, 0x44, 0x41, 0x54, 0x78, (byte)0xDA, 0x63, 0x60, 0x00, 0x02, 0x00, 0x00,
                0x05, 0x00, 0x01, (byte)0xE2, 0x26, 0x05, (byte)0x9B, 0x00, 0x00, 0x00, 0x00, 0x49, 0x45, 0x4E, 0x44, (byte)0xAE, 0x42, 0x60, (byte)0x82
        };
    }
}