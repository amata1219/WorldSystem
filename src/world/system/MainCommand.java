package world.system;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.scheduler.BukkitRunnable;

public class MainCommand implements TabExecutor {
    WorldSystem plugin;

    public MainCommand(WorldSystem plugin) {
        this.plugin = WorldSystem.plugin;
        plugin = this.plugin;
    }

    public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
        return null;
    }

    public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.GREEN + "WorldSystem");
            sender.sendMessage(ChatColor.AQUA + "開発環境");
            sender.sendMessage(ChatColor.WHITE + "- Eclipse 4.4 Luna");
            sender.sendMessage(ChatColor.WHITE + "- Java 1.8");
            sender.sendMessage(ChatColor.AQUA + "バージョン情報");
            sender.sendMessage(ChatColor.WHITE + "- WorldSystem Ver." + this.plugin.getDescription().getVersion());
            sender.sendMessage(ChatColor.WHITE + "- Spigot 1.11.2");
            sender.sendMessage(ChatColor.AQUA + "その他");
            sender.sendMessage(ChatColor.WHITE + "/ws help と入力すると本プラグインのコマンド一覧を表示します。");
            return true;
        } else {
            if (args[0].equalsIgnoreCase("help")) {
                sender.sendMessage(ChatColor.GREEN + "/ws");
                sender.sendMessage(ChatColor.WHITE + "WorldSystemの詳細情報を表示します。");
                sender.sendMessage(ChatColor.GREEN + "/ws help");
                sender.sendMessage(ChatColor.WHITE + "WorldSystemのコマンド一覧を表示します。");
                sender.sendMessage(ChatColor.GREEN + "/ws config [help|reload|list|コンフィグキー] <真偽値|文章>");
                sender.sendMessage(ChatColor.WHITE + "コンフィグ編集をコマンドから行えます。詳しい使用方法は、/ws config help にて確認出来ます。");
                sender.sendMessage(ChatColor.GREEN + "/ws task [start|stop]");
                sender.sendMessage(ChatColor.WHITE + "ブースター使用制限用TPS監視タスクを開始・終了させます。");
                sender.sendMessage(ChatColor.GREEN + "/ws cubworld");
                sender.sendMessage(ChatColor.WHITE + "ブースター使用制限ワールド一覧を表示します。");
                sender.sendMessage(ChatColor.GREEN + "/ws tps");
                sender.sendMessage(ChatColor.WHITE + "サーバーの最新のTPSを取得します。");
                sender.sendMessage(ChatColor.GREEN + "/ws regen");
                sender.sendMessage(ChatColor.WHITE + "WorldEdit専用ツールで選択した範囲を再生成します。");
                sender.sendMessage(ChatColor.GREEN + "/ws deleteclaims");
                sender.sendMessage(ChatColor.WHITE + "期限切れの全ての保護を削除し再生成します(※面積がしきい値以上であれば処理はキャンセルされます)");
                sender.sendMessage(ChatColor.GREEN + "/ws gate [create|delete] <ゲート名>");
                sender.sendMessage(ChatColor.WHITE + "WorldEdit専用ツールで選択した範囲に、インベントリが空の場合通行可能なゲートを作成または、削除します。");
            } else if (args[0].equalsIgnoreCase("task")) {
                if (args.length == 2) {
                    if (args[1].equalsIgnoreCase("start")) {
                        this.plugin.startTaskRunnable();
                        sender.sendMessage("全タスクを開始しました。");
                        return true;
                    }

                    if (args[1].equalsIgnoreCase("stop")) {
                        this.plugin.stopAllTaskRunnable();
                        sender.sendMessage("全タスクを終了しました。");
                        return true;
                    }

                    return true;
                }

                if (args[0].equalsIgnoreCase("cubworld")) {
                    sender.sendMessage("ブースター使用制限ワールドは、" + this.plugin.getConfig().getString("world") + "に設定されています。");
                    return true;
                }

                if (args[0].equalsIgnoreCase("config")) {
                    int var10000 = args.length;
                }
            } else if (args[0].equalsIgnoreCase("gate")) {
                if (args.length == 2) {
                    if (args[1].equalsIgnoreCase("list")) {
                        sender.sendMessage(this.plugin.getConfig().getStringList("InvCheckGateList").toString());
                        return true;
                    }
                } else if (args.length == 3) {
                    if (!(sender instanceof Player)) {
                        sender.sendMessage(ChatColor.RED + "ゲーム内から実行して下さい。");
                        return true;
                    }

                    InvCheckGate icg = new InvCheckGate();
                    if (args[1].equalsIgnoreCase("create")) {
                        icg.CreateGate((Player)sender, args[2]);
                        return true;
                    }

                    if (args[1].equalsIgnoreCase("delete")) {
                        icg.DeleteGate(sender, args[2]);
                        return true;
                    }
                }
            } else if (args[0].equalsIgnoreCase("gate+")) {
                InvCheckGatePlus icgp = new InvCheckGatePlus();
                if (args.length == 2) {
                    if (args[1].equalsIgnoreCase("put")) {
                        icgp.PutItems((Player)sender);
                        return true;
                    }

                    if (args[1].equalsIgnoreCase("list")) {
                        sender.sendMessage(this.plugin.getConfig().getStringList("InvCheckGatePlusList").toString());
                        return true;
                    }

                    if (args[1].equalsIgnoreCase("itemlist")) {
                        sender.sendMessage(this.plugin.getConfig().getStringList("InvCheckGatePlusItems").toString());
                        return true;
                    }
                } else if (args.length == 3) {
                    if (!(sender instanceof Player)) {
                        sender.sendMessage(ChatColor.RED + "ゲーム内から実行して下さい。");
                        return true;
                    }

                    if (args[1].equalsIgnoreCase("create")) {
                        icgp.CreateGatePlus((Player)sender, args[2]);
                        return true;
                    }

                    if (args[1].equalsIgnoreCase("delete")) {
                        icgp.DeleteGatePlus(sender, args[2]);
                        return true;
                    }

                    if (args[1].equalsIgnoreCase("remove")) {
                        icgp.RemoveItems(sender, args[2]);
                        return true;
                    }
                }
            } else {
                int i;
                if (args[0].equalsIgnoreCase("kill")) {
                    if (args.length == 2) {
                        if (!(sender instanceof BlockCommandSender)) {
                            sender.sendMessage(ChatColor.RED + "コマンドブロックから実行して下さい。");
                            return true;
                        }

                        try {
                            int var12 = Integer.valueOf(args[1]);
                        } catch (NumberFormatException var10) {
                            sender.sendMessage(ChatColor.RED + "半径は数字で指定して下さい。");
                            return true;
                        }

                        BlockCommandSender bcs = (BlockCommandSender)sender;
                        Snowball sb = (Snowball)bcs.getBlock().getLocation().getWorld().spawnEntity(bcs.getBlock().getLocation(), EntityType.SNOWBALL);
                        i = Integer.valueOf(args[1]);
                        if (i > 50) {
                            return true;
                        }

                        Iterator var9 = sb.getNearbyEntities((double)i, (double)i, (double)i).iterator();

                        while(var9.hasNext()) {
                            Entity entity = (Entity)var9.next();
                            if (entity.getType() != EntityType.PLAYER && entity.getType() != EntityType.DROPPED_ITEM && entity != null) {
                                entity.remove();
                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("delaytimer")) {
                    final ArrayList<String> commands = new ArrayList();
                    if (this.isDigit(args[1])) {
                        for(i = 2; i < args.length; ++i) {
                            commands.add(args[i]);
                        }

                        int ticks = Integer.parseInt(args[1]);
                        BukkitRunnable runnable = new BukkitRunnable() {
                            public void run() {
                                StringBuilder builder = new StringBuilder();
                                Iterator var3 = commands.iterator();

                                while(var3.hasNext()) {
                                    String com = (String)var3.next();
                                    builder.append(com + " ");
                                }

                                Bukkit.dispatchCommand(sender, builder.toString().trim());
                            }
                        };
                        runnable.runTaskLater(this.plugin, (long)ticks);
                        return true;
                    }
                }
            }

            return true;
        }
    }

    private boolean isDigit(String value) {
        return value.matches("^[0-9]{1,9}$");
    }
}
