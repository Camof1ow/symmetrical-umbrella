package com.example.japanesenamegenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

class XssPreventionSerializerTest {

    private PolicyFactory policy;

    @BeforeEach
    void setUp() {
        policy = Sanitizers.BLOCKS
            .and(Sanitizers.FORMATTING)
            .and(Sanitizers.LINKS)
            .and(Sanitizers.STYLES)
            .and(Sanitizers.IMAGES);
    }

    @Test
    void testPolicyDirectly() {
        String input = "<p>Test <b>bold</b> and <i>italic</i> and <script>alert('xss')</script></p>";
        String result = policy.sanitize(input);
        System.out.println("Policy direct result: " + result);
        assertEquals("<p>Test <b>bold</b> and <i>italic</i> and </p>", result);
    }

    private void runTest(String testName, String input, String expected) {
        String result = policy.sanitize(input);
        System.out.println(testName + ":");
        System.out.println("Input   : " + input);
        System.out.println("Expected: " + expected);
        System.out.println("Actual  : " + result);
        System.out.println("Test " + (expected.equals(result) ? "PASSED" : "FAILED"));
        System.out.println();
        assertEquals(expected, result);
    }

    @Test
    void testSanitizeBasicHtml() {
        runTest("Basic HTML",
            "<p>This is <b>bold</b> and <i>italic</i> text.</p>",
            "<p>This is <b>bold</b> and <i>italic</i> text.</p>");
    }

    @Test
    void testSanitizeScriptTag() {
        runTest("Script Tag",
            "<script>alert('XSS');</script>",
            "");
    }

    @Test
    void testSanitizeOnEventAttributes() {
        runTest("OnEvent Attributes",
            "<a href='#' onclick='alert(\"XSS\")'>Click me</a>",
            "<a href=\"#\" rel=\"nofollow\">Click me</a>");
    }

    @Test
    void testSanitizeImgTag() {
        runTest("Img Tag",
            "<img src='javascript:alert(\"XSS\")' />",
            "");
    }

    @Test
    void testSanitizeComplexNestedTags() {
        runTest("Complex Nested Tags",
            "<div><script>alert('XSS');</script><p>Safe <b>content</b></p></div>",
            "<div><p>Safe <b>content</b></p></div>");
    }

    @Test
    void testSanitizeEncodedScript() {
        runTest("Encoded Script",
            "<p>This is a &#60;script&#62;alert('XSS');&#60;/script&#62; test.</p>",
            "<p>This is a &lt;script&gt;alert('XSS');&lt;/script&gt; test.</p>");
    }

    @Test
    void testSanitizeAllowedLink() {
        runTest("Allowed Link",
            "<a href='https://www.example.com'>Safe Link</a>",
            "<a href=\"https://www.example.com\" rel=\"nofollow\">Safe Link</a>");
    }

    @Test
    void testSanitizeDisallowedProtocol() {
        runTest("Disallowed Protocol",
            "<a href='javascript:alert(\"XSS\")'>Unsafe Link</a>",
            "Unsafe Link");
    }

    @Test
    void testSanitizePreserveContent() {
        runTest("Preserve Content",
            "<script>alert('XSS');</script>Preserved Content",
            "Preserved Content");
    }

    @Test
    void testSanitizeStyleTag() {
        runTest("Style Tag",
            "<style>body { background: url('javascript:alert(\"XSS\")'); }</style>",
            "");
    }


}