package com.glitchcog.fontificator.config;

import java.awt.Rectangle;
import java.util.Properties;

import com.glitchcog.fontificator.config.loadreport.LoadConfigReport;

/**
 * The Configuration for the Chat Window
 * 
 * @author Matt Yanos
 */
public class ConfigChat extends Config
{
    public static final int MIN_CHROMA_CORNER_RADIUS = 0;
    public static final int MAX_CHROMA_CORNER_RADIUS = 128;

    /**
     * Whether the chat is scrollable by rolling the mouse wheel
     */
    private Boolean scrollable;

    /**
     * Whether the chat window is resizable by dragging the mouse on its borders
     */
    private Boolean resizable;

    /**
     * Whether to remember the chat window position
     */
    private Boolean rememberPosition;

    /**
     * The remembered chat window position X coordinate
     */
    private Integer chatWindowPositionX;

    /**
     * The remembered chat window position Y coordinate
     */
    private Integer chatWindowPositionY;

    /**
     * When the chat has too few messages to fill the available space, this determines whether the chat starts from the
     * bottom and builds upward, or from the top and builds downward
     */
    private Boolean chatFromBottom;

    /**
     * The width of the chat window in pixels
     */
    private Integer width;

    /**
     * The height of the chat window in pixels
     */
    private Integer height;

    /**
     * Whether the chroma border is enabled
     */
    private Boolean chromaEnabled;

    /**
     * Whether the chroma border is inverted
     */
    private Boolean chromaInvert;

    /**
     * Used to represent the border, so the x and y are the left and top borders, and w and h are the right and bottom
     * borders
     */
    private Rectangle chromaBorder;

    /**
     * How rounded the corners of the chroma border should be
     */
    private Integer chromaCornerRadius;

    /**
     * Whether the chat should scroll in reverse
     */
    private Boolean reverseScrolling;

    /**
     * Whether the chat window should be set to always on top of other windows on the screen
     */
    private Boolean alwaysOnTop;

    /**
     * Whether the graphics of the chat panel should be anti-aliased when scaled
     */
    private Boolean antiAlias;

    @Override
    public void reset()
    {
        this.scrollable = null;
        this.resizable = null;
        this.rememberPosition = null;
        this.chatWindowPositionX = null;
        this.chatWindowPositionY = null;
        this.chatFromBottom = null;
        this.width = null;
        this.height = null;
        this.chromaEnabled = null;
        this.chromaInvert = null;
        this.chromaBorder = null;
        this.chromaCornerRadius = null;
        this.reverseScrolling = null;
        this.alwaysOnTop = null;
        this.antiAlias = null;
    }

    public LoadConfigReport validateDimStrings(LoadConfigReport report, String widthStr, String heightStr)
    {
        validateIntegerWithLimitString(FontificatorProperties.KEY_CHAT_WIDTH, widthStr, 1, report);
        validateIntegerWithLimitString(FontificatorProperties.KEY_CHAT_HEIGHT, heightStr, 1, report);
        return report;
    }

    public LoadConfigReport validateChromaDimStrings(LoadConfigReport report, String leftStr, String topStr, String rightStr, String botStr)
    {
        validateIntegerWithLimitString(FontificatorProperties.KEY_CHAT_CHROMA_LEFT, leftStr, 0, report);
        validateIntegerWithLimitString(FontificatorProperties.KEY_CHAT_CHROMA_TOP, topStr, 0, report);
        validateIntegerWithLimitString(FontificatorProperties.KEY_CHAT_CHROMA_RIGHT, rightStr, 0, report);
        validateIntegerWithLimitString(FontificatorProperties.KEY_CHAT_CHROMA_BOTTOM, botStr, 0, report);
        return report;
    }

    public LoadConfigReport validateStrings(LoadConfigReport report, String widthStr, String heightStr, String chromaLeftStr, String chromaTopStr, String chromaRightStr, String chromaBottomStr, String chromaCornerStr, String scrollBool, String reverseScrollBool, String resizeBool, String remPosBool, String fromBottomBool, String chromaBool, String invertBool, String topBool)
    {
        validateBooleanStrings(report, scrollBool, reverseScrollBool, resizeBool, remPosBool, fromBottomBool, chromaBool, invertBool, topBool);

        validateDimStrings(report, widthStr, heightStr);
        validateChromaDimStrings(report, chromaLeftStr, chromaTopStr, chromaRightStr, chromaBottomStr);
        validateIntegerWithLimitString(FontificatorProperties.KEY_CHAT_CHROMA_CORNER, chromaCornerStr, 0, report);

        return report;
    }

    @Override
    public LoadConfigReport load(Properties props, LoadConfigReport report)
    {
        this.props = props;

        reset();

        // Check that the values exist
        baseValidation(props, FontificatorProperties.CHAT_KEYS, report);

        if (report.isErrorFree())
        {
            final String widthStr = props.getProperty(FontificatorProperties.KEY_CHAT_WIDTH);
            final String heightStr = props.getProperty(FontificatorProperties.KEY_CHAT_HEIGHT);
            final String chromaLeftStr = props.getProperty(FontificatorProperties.KEY_CHAT_CHROMA_LEFT);
            final String chromaTopStr = props.getProperty(FontificatorProperties.KEY_CHAT_CHROMA_TOP);
            final String chromaRightStr = props.getProperty(FontificatorProperties.KEY_CHAT_CHROMA_RIGHT);
            final String chromaBottomStr = props.getProperty(FontificatorProperties.KEY_CHAT_CHROMA_BOTTOM);

            final String chromaCornerStr = props.getProperty(FontificatorProperties.KEY_CHAT_CHROMA_CORNER);

            final String scrollBool = props.getProperty(FontificatorProperties.KEY_CHAT_SCROLL);
            final String resizeBool = props.getProperty(FontificatorProperties.KEY_CHAT_RESIZABLE);
            final String remPosBool = props.getProperty(FontificatorProperties.KEY_CHAT_POSITION);
            final String fromBottomBool = props.getProperty(FontificatorProperties.KEY_CHAT_FROM_BOTTOM);
            final String reverseScrollBool = props.getProperty(FontificatorProperties.KEY_CHAT_REVERSE_SCROLLING);
            final String chromaBool = props.getProperty(FontificatorProperties.KEY_CHAT_CHROMA_ENABLED);
            final String invertBool = props.getProperty(FontificatorProperties.KEY_CHAT_INVERT_CHROMA);
            final String topBool = props.getProperty(FontificatorProperties.KEY_CHAT_ALWAYS_ON_TOP);

            // Check that the values are valid
            validateStrings(report, widthStr, heightStr, chromaLeftStr, chromaTopStr, chromaRightStr, chromaBottomStr, chromaCornerStr, scrollBool, reverseScrollBool, resizeBool, remPosBool, fromBottomBool, chromaBool, invertBool, topBool);

            // Fill the values
            if (report.isErrorFree())
            {
                width = Integer.parseInt(widthStr);
                height = Integer.parseInt(heightStr);

                chromaCornerRadius = Integer.parseInt(chromaCornerStr);

                int left = Integer.parseInt(chromaLeftStr);
                int top = Integer.parseInt(chromaTopStr);
                int right = Integer.parseInt(chromaRightStr);
                int bot = Integer.parseInt(chromaBottomStr);
                setChromaBorder(left, top, right, bot);

                scrollable = evaluateBooleanString(props, FontificatorProperties.KEY_CHAT_SCROLL, report);
                resizable = evaluateBooleanString(props, FontificatorProperties.KEY_CHAT_RESIZABLE, report);
                rememberPosition = evaluateBooleanString(props, FontificatorProperties.KEY_CHAT_POSITION, report);
                chatWindowPositionX = evaluateIntegerString(props, FontificatorProperties.KEY_CHAT_POSITION_X, report);
                chatWindowPositionY = evaluateIntegerString(props, FontificatorProperties.KEY_CHAT_POSITION_Y, report);
                chatFromBottom = evaluateBooleanString(props, FontificatorProperties.KEY_CHAT_FROM_BOTTOM, report);
                reverseScrolling = evaluateBooleanString(props, FontificatorProperties.KEY_CHAT_REVERSE_SCROLLING, report);
                chromaEnabled = evaluateBooleanString(props, FontificatorProperties.KEY_CHAT_CHROMA_ENABLED, report);
                chromaInvert = evaluateBooleanString(props, FontificatorProperties.KEY_CHAT_INVERT_CHROMA, report);
                alwaysOnTop = evaluateBooleanString(props, FontificatorProperties.KEY_CHAT_ALWAYS_ON_TOP, report);
                antiAlias = evaluateBooleanString(props, FontificatorProperties.KEY_CHAT_ANTIALIAS, report);
            }
        }

        return report;
    }

    public boolean isScrollable()
    {
        return scrollable;
    }

    public void setScrollable(boolean scrollable)
    {
        this.scrollable = scrollable;
        props.setProperty(FontificatorProperties.KEY_CHAT_SCROLL, Boolean.toString(scrollable));
    }

    public boolean isResizable()
    {
        return resizable;
    }

    public void setResizable(boolean resizable)
    {
        this.resizable = resizable;
        props.setProperty(FontificatorProperties.KEY_CHAT_RESIZABLE, Boolean.toString(resizable));
    }

    public boolean isRememberPosition()
    {
        return rememberPosition;
    }

    public void setRememberPosition(boolean rememberPosition)
    {
        this.rememberPosition = rememberPosition;
        props.setProperty(FontificatorProperties.KEY_CHAT_POSITION, Boolean.toString(rememberPosition));
    }

    public int getChatWindowPositionX()
    {
        return chatWindowPositionX == null ? 0 : chatWindowPositionX;
    }

    public void setChatWindowPositionX(int chatWindowPositionX)
    {
        this.chatWindowPositionX = chatWindowPositionX;
        props.setProperty(FontificatorProperties.KEY_CHAT_POSITION_X, Integer.toString(chatWindowPositionX));
    }

    public int getChatWindowPositionY()
    {
        return chatWindowPositionY == null ? 0 : chatWindowPositionY;
    }

    public void setChatWindowPositionY(int chatWindowPositionY)
    {
        this.chatWindowPositionY = chatWindowPositionY;
        props.setProperty(FontificatorProperties.KEY_CHAT_POSITION_Y, Integer.toString(chatWindowPositionY));
    }

    public boolean isChatFromBottom()
    {
        return chatFromBottom;
    }

    public void setChatFromBottom(boolean chatFromBottom)
    {
        this.chatFromBottom = chatFromBottom;
        props.setProperty(FontificatorProperties.KEY_CHAT_FROM_BOTTOM, Boolean.toString(chatFromBottom));
    }

    public boolean isReverseScrolling()
    {
        return reverseScrolling;
    }

    public void setReverseScrolling(boolean reverseScrolling)
    {
        this.reverseScrolling = reverseScrolling;
        props.setProperty(FontificatorProperties.KEY_CHAT_REVERSE_SCROLLING, Boolean.toString(reverseScrolling));
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
        props.setProperty(FontificatorProperties.KEY_CHAT_WIDTH, Integer.toString(width));
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
        props.setProperty(FontificatorProperties.KEY_CHAT_HEIGHT, Integer.toString(height));
    }

    public boolean isAlwaysOnTop()
    {
        return alwaysOnTop;
    }

    public void setAlwaysOnTop(boolean alwaysOnTop)
    {
        this.alwaysOnTop = alwaysOnTop;
        props.setProperty(FontificatorProperties.KEY_CHAT_ALWAYS_ON_TOP, Boolean.toString(alwaysOnTop));
    }

    public boolean isAntiAlias()
    {
        return antiAlias;
    }

    public void setAntiAlias(boolean antiAlias)
    {
        this.antiAlias = antiAlias;
        props.setProperty(FontificatorProperties.KEY_CHAT_ANTIALIAS, Boolean.toString(antiAlias));
    }

    public Boolean isChromaEnabled()
    {
        return chromaEnabled;
    }

    public void setChromaEnabled(Boolean chromaEnabled)
    {
        this.chromaEnabled = chromaEnabled;
        props.setProperty(FontificatorProperties.KEY_CHAT_CHROMA_ENABLED, Boolean.toString(chromaEnabled));
    }

    public boolean isChromaInvert()
    {
        return chromaInvert;
    }

    public void setChromaInvert(boolean chromaInvert)
    {
        this.chromaInvert = chromaInvert;
        props.setProperty(FontificatorProperties.KEY_CHAT_INVERT_CHROMA, Boolean.toString(chromaInvert));
    }

    public Rectangle getChromaBorder()
    {
        return chromaBorder;
    }

    public void setChromaBorder(int left, int top, int right, int bottom)
    {
        setChromaBorder(new Rectangle(left, top, right, bottom));
    }

    public void setChromaBorder(Rectangle chromaBorder)
    {
        this.chromaBorder = chromaBorder;
        props.setProperty(FontificatorProperties.KEY_CHAT_CHROMA_LEFT, Integer.toString(chromaBorder.x));
        props.setProperty(FontificatorProperties.KEY_CHAT_CHROMA_TOP, Integer.toString(chromaBorder.y));
        props.setProperty(FontificatorProperties.KEY_CHAT_CHROMA_RIGHT, Integer.toString(chromaBorder.width));
        props.setProperty(FontificatorProperties.KEY_CHAT_CHROMA_BOTTOM, Integer.toString(chromaBorder.height));
    }

    public int getChromaCornerRadius()
    {
        return chromaCornerRadius;
    }

    public void setChromaCornerRadius(int chromaCornerRadius)
    {
        this.chromaCornerRadius = chromaCornerRadius;
        props.setProperty(FontificatorProperties.KEY_CHAT_CHROMA_CORNER, Integer.toString(chromaCornerRadius));
    }

}
