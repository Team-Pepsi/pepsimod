/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017 Team Pepsi
 *
 * Permission is hereby granted to any persons and/or organizations using this software to copy, modify, merge, publish, and distribute it.
 * Said persons and/or organizations are not allowed to use the software or any derivatives of the work for commercial use or any other means to generate income, nor are they allowed to claim this software as their own.
 *
 * The persons and/or organizations are also disallowed from sub-licensing and/or trademarking this software without explicit permission from Team Pepsi.
 *
 * Any persons and/or organizations using this software must disclose their source code and have it publicly available, include this license, provide sufficient credit to the original authors of the project (IE: Team Pepsi), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.daporkchop.pepsimod.wdl.update;

import net.daporkchop.pepsimod.wdl.VersionConstants;
import net.daporkchop.pepsimod.wdl.WDL;
import net.daporkchop.pepsimod.wdl.WDLMessageTypes;
import net.daporkchop.pepsimod.wdl.WDLMessages;
import net.daporkchop.pepsimod.wdl.update.Release.HashData;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.ClickEvent.Action;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Performs the update checking.
 */
public class WDLUpdateChecker extends Thread {
    private static final String FORUMS_THREAD_USAGE_LINK = "http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/2520465#Usage";
    private static final String WIKI_LINK = "https://github.com/pokechu22/WorldDownloader/wiki";
    private static final String GITHUB_LINK = "https://github.com/pokechu22/WorldDownloader";
    private static final String REDISTRIBUTION_LINK = "http://pokechu22.github.io/WorldDownloader/redistribution";
    private static final String SMR_LINK = "http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/mods-discussion/2314237";
    private static final String SNAPSHOT_SUFFIX = "-SNAPSHOT";
    /**
     * Has the update check started?
     */
    private static volatile boolean started = false;
    /**
     * Has the update check finished?
     */
    private static volatile boolean finished = false;
    /**
     * Did something go wrong with the update check?
     */
    private static volatile boolean failed = false;
    /**
     * If something went wrong with the update check, what was it?
     */
    @Nullable
    private static volatile String failReason = null;
    /**
     * List of releases.  May be null if the checker has not finished.
     */
    @Nullable
    private static volatile List<Release> releases;
    /**
     * The release that is currently running.
     * <p>
     * May be null.
     */
    @Nullable
    private static volatile Release runningRelease;

    private WDLUpdateChecker() {
        super("World Downloader update check thread");
    }

    /**
     * Gets the current list of releases. May be null if the checker has not
     * finished.
     */
    @Nullable
    public static List<Release> getReleases() {
        return releases;
    }

    /**
     * Gets the current release.  May be null if the checker has not finished
     * or if the current version isn't released.
     */
    @Nullable
    public static Release getRunningRelease() {
        return runningRelease;
    }

    /**
     * Calculates the release that should be used based off of the user's options.
     * <p>
     * May be null if the checker has not finished.
     */
    @Nullable
    public static Release getRecomendedRelease() {
        if (releases == null || releases.isEmpty()) {
            return null;
        }

        String version = "v" + VersionConstants.getModVersion();
        if (isSnapshot(version)) {
            // Running a snapshot version?  Check if a full version was released.
            String realVersion = getRealVersion(version);
            boolean hasRelease = false;
            for (Release release : releases) {
                if (realVersion.equals(release.tag)) {
                    hasRelease = true;
                }
            }
            if (!hasRelease) {
                // No full release?  OK, don't recommend they go backwards.
                return null;
                // If there is a full release, we'd recommend the latest release.
            }
        }
        return releases.get(0);
    }

    /**
     * Is there a new version that should be used?
     * <p>
     * True if the running release is not null and if the recommended
     * release is not the running release.
     * <p>
     * The return value of this method may change as the update checker
     * runs.
     */
    public static boolean hasNewVersion() {
        if (releases == null || releases.isEmpty()) {
            // Hasn't finished running yet.
            return false;
        }
        Release recomendedRelease = getRecomendedRelease();
        // Note: runningRelease may be unknown; getRecomendedRelease handles that (for snapshots)
        // However, if both are null, we don't want to recommend updating to null; that's pointless
        if (recomendedRelease == null) {
            return false;
        }
        return runningRelease != recomendedRelease;
    }

    /**
     * Call once the world has loaded.  Will check and start a new update checker
     * if needed.
     */
    public static void startIfNeeded() {
        if (!started) {
            started = true;

            new WDLUpdateChecker().start();
        }
    }

    /**
     * Has the update check finished?
     */
    public static boolean hasFinishedUpdateCheck() {
        return finished;
    }

    /**
     * Did something go wrong with the update check?
     */
    public static boolean hasUpdateCheckFailed() {
        return failed;
    }

    /**
     * If the update check failed, why?
     */
    public static String getUpdateCheckFailReason() {
        return failReason;
    }

    /**
     * Checks if a version is a snapshot build.
     *
     * @param version The version to check
     * @return true if the version is a SNAPSHOT build
     */
    private static boolean isSnapshot(@Nonnull String version) {
        return version.endsWith(SNAPSHOT_SUFFIX);
    }

    /**
     * For a snapshot version, gets the version name for the real version.
     *
     * @param version The version to use. <strong>Must</strong>
     *                {@linkplain #isSnapshot(String) be a snapshot version}.
     * @return the regular version name for that snapshot, without the SNAPSHOT suffix.
     */
    @Nonnull
    private static String getRealVersion(@Nonnull String version) {
        assert isSnapshot(version) : "getRealVersion should only be used with snapshots; got " + version;

        return version.substring(0, version.length() - SNAPSHOT_SUFFIX.length());
    }

    @Override
    public void run() {
        try {
            if (!WDL.globalProps.getProperty("TutorialShown").equals("true")) {
                sleep(5000);

                TextComponentTranslation success = new TextComponentTranslation(
                        "net.daporkchop.pepsimod.wdl.intro.success");
                TextComponentTranslation mcfThread = new TextComponentTranslation(
                        "net.daporkchop.pepsimod.wdl.intro.forumsLink");
                mcfThread.getStyle().setColor(TextFormatting.BLUE).setUnderlined(true)
                        .setClickEvent(new ClickEvent(Action.OPEN_URL, FORUMS_THREAD_USAGE_LINK));
                TextComponentTranslation wikiLink = new TextComponentTranslation(
                        "net.daporkchop.pepsimod.wdl.intro.wikiLink");
                wikiLink.getStyle().setColor(TextFormatting.BLUE).setUnderlined(true)
                        .setClickEvent(new ClickEvent(Action.OPEN_URL, WIKI_LINK));
                TextComponentTranslation usage = new TextComponentTranslation(
                        "net.daporkchop.pepsimod.wdl.intro.usage", mcfThread, wikiLink);
                TextComponentTranslation githubRepo = new TextComponentTranslation(
                        "net.daporkchop.pepsimod.wdl.intro.githubRepo");
                githubRepo.getStyle().setColor(TextFormatting.BLUE).setUnderlined(true)
                        .setClickEvent(new ClickEvent(Action.OPEN_URL, GITHUB_LINK));
                TextComponentTranslation contribute = new TextComponentTranslation(
                        "net.daporkchop.pepsimod.wdl.intro.contribute", githubRepo);
                TextComponentTranslation redistributionList = new TextComponentTranslation(
                        "net.daporkchop.pepsimod.wdl.intro.redistributionList");
                redistributionList.getStyle().setColor(TextFormatting.BLUE).setUnderlined(true)
                        .setClickEvent(new ClickEvent(Action.OPEN_URL, REDISTRIBUTION_LINK));
                TextComponentTranslation warning = new TextComponentTranslation(
                        "net.daporkchop.pepsimod.wdl.intro.warning");
                warning.getStyle().setColor(TextFormatting.DARK_RED).setBold(true);
                TextComponentTranslation illegally = new TextComponentTranslation(
                        "net.daporkchop.pepsimod.wdl.intro.illegally");
                illegally.getStyle().setColor(TextFormatting.DARK_RED).setBold(true);
                TextComponentTranslation stolen = new TextComponentTranslation(
                        "net.daporkchop.pepsimod.wdl.intro.stolen", warning, redistributionList, illegally);
                TextComponentTranslation smr = new TextComponentTranslation(
                        "net.daporkchop.pepsimod.wdl.intro.stopModReposts");
                smr.getStyle().setColor(TextFormatting.BLUE).setUnderlined(true)
                        .setClickEvent(new ClickEvent(Action.OPEN_URL, SMR_LINK));
                TextComponentTranslation stolenBeware = new TextComponentTranslation(
                        "net.daporkchop.pepsimod.wdl.intro.stolenBeware", smr);

                WDLMessages.chatMessage(WDLMessageTypes.UPDATES, success);
                WDLMessages.chatMessage(WDLMessageTypes.UPDATES, usage);
                WDLMessages.chatMessage(WDLMessageTypes.UPDATES, contribute);
                WDLMessages.chatMessage(WDLMessageTypes.UPDATES, stolen);
                WDLMessages.chatMessage(WDLMessageTypes.UPDATES, stolenBeware);

                WDL.globalProps.setProperty("TutorialShown", "true");
                WDL.saveGlobalProps();
            }

            sleep(5000);

            releases = GithubInfoGrabber.getReleases();
            WDLMessages.chatMessageTranslated(WDLMessageTypes.UPDATE_DEBUG,
                    "net.daporkchop.pepsimod.wdl.messages.updates.releaseCount", releases.size());

            if (releases.isEmpty()) {
                failed = true;
                failReason = "No releases found.";
                return;
            }

            String version = VersionConstants.getModVersion();
            String currentTag = "v" + version;
            for (int i = 0; i < releases.size(); i++) {
                Release release = releases.get(i);

                if (release.tag.equalsIgnoreCase(currentTag)) {
                    runningRelease = release;
                }
            }

            if (runningRelease == null) {
                if (!isSnapshot(version)) {
                    WDLMessages.chatMessageTranslated(WDLMessageTypes.UPDATE_DEBUG,
                            "net.daporkchop.pepsimod.wdl.messages.updates.failedToFindMatchingRelease",
                            currentTag);
                } else {
                    WDLMessages.chatMessageTranslated(WDLMessageTypes.UPDATE_DEBUG,
                            "net.daporkchop.pepsimod.wdl.messages.updates.failedToFindMatchingRelease.snapshot",
                            currentTag, getRealVersion(version));
                }
                // Wait until the new version check finishes before returning.
            }

            if (hasNewVersion()) {
                Release recomendedRelease = getRecomendedRelease();

                TextComponentTranslation updateLink = new TextComponentTranslation(
                        "net.daporkchop.pepsimod.wdl.messages.updates.newRelease.updateLink");
                updateLink.getStyle().setColor(TextFormatting.BLUE)
                        .setUnderlined(true).setClickEvent(
                        new ClickEvent(Action.OPEN_URL,
                                recomendedRelease.URL));

                // Show the new version available message, and give a link.
                WDLMessages.chatMessageTranslated(WDLMessageTypes.UPDATES,
                        "net.daporkchop.pepsimod.wdl.messages.updates.newRelease", currentTag,
                        recomendedRelease.tag, updateLink);
            }

            if (runningRelease == null) {
                // Can't hash without a release, but that's a normal condition (unlike below)
                return;
            }

            if (runningRelease.hiddenInfo == null) {
                WDLMessages.chatMessageTranslated(WDLMessageTypes.UPDATE_DEBUG,
                        "net.daporkchop.pepsimod.wdl.messages.updates.failedToFindMetadata",
                        currentTag);
                return;
            }
            //Check the hashes, and list any failing ones.
            Map<HashData, Object> failed = new HashMap<>();

            hashLoop:
            for (HashData data : runningRelease.hiddenInfo.hashes) {
                try {
                    String hash = ClassHasher.hash(data.relativeTo, data.file);

                    for (String validHash : data.validHashes) {
                        if (validHash.equalsIgnoreCase(hash)) {
                            // Labeled continues / breaks _are_ a thing.
                            // This just continues the outer loop.
                            continue hashLoop;
                        }
                    }

					/*WDLMessages.chatMessageTranslated(
                            WDLMessageTypes.UPDATE_DEBUG,
							"net.daporkchop.pepsimod.wdl.messages.updates.incorrectHash", data.file,
							data.relativeTo, Arrays.toString(data.validHashes),
							hash);*/

                    failed.put(data, hash);
                    continue;
                } catch (Exception e) {
					/*WDLMessages.chatMessageTranslated(
							WDLMessageTypes.UPDATE_DEBUG,
							"net.daporkchop.pepsimod.wdl.messages.updates.hashException", data.file,
							data.relativeTo, Arrays.toString(data.validHashes),
							e);*/

                    failed.put(data, e);
                }
            }

            if (failed.size() > 0) {
                TextComponentTranslation mcfThread = new TextComponentTranslation(
                        "net.daporkchop.pepsimod.wdl.intro.forumsLink");
                mcfThread.getStyle().setColor(TextFormatting.BLUE)
                        .setUnderlined(true).setClickEvent(
                        new ClickEvent(Action.OPEN_URL,
                                FORUMS_THREAD_USAGE_LINK));
                WDLMessages.chatMessageTranslated(WDLMessageTypes.UPDATES,
                        "net.daporkchop.pepsimod.wdl.messages.updates.badHashesFound", mcfThread);
            }
        } catch (Exception e) {
            WDLMessages.chatMessageTranslated(WDLMessageTypes.UPDATE_DEBUG,
                    "net.daporkchop.pepsimod.wdl.messages.updates.updateCheckError", e);

            failed = true;
            failReason = e.toString();
        } finally {
            finished = true;
        }
    }
}
