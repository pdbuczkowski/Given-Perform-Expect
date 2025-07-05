# Given-Perform-Expect

[![](https://jitpack.io/v/pdbuczkowski/Given-Perform-Expect.svg)](https://jitpack.io/#pdbuczkowski/Given-Perform-Expect)

A minimalist Kotlin test helper library designed for writing clean, expressive, and structured unit tests in the classic `Given / When / Then` pattern.

---

## ✨ Example

```kotlin
test(
    given = { testedObject = TestedClass() },
    perform = { testedObject.initialize() },
    actual = { testedObject.state },
    expect = { State.Initialized }
)
```

This structure helps organize your test logic into clearly separated phases:
- `given` – Setup initial state
- `perform` – Execute the action under test
- `actual` – Get the result of the test
- `expect` – Define the expected outcome

See [ExampleUnitTest.kt](app/src/test/java/com/example/myapplication/ExampleUnitTest.kt) for more examples.

---

## 🚀 How to use it in your project (via [JitPack](https://jitpack.io/#pdbuczkowski/Given-Perform-Expect))

### 1. Add the JitPack repository

In your `settings.gradle.kts` or `build.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        maven("https://jitpack.io")
        mavenCentral()
    }
}
```

Or in Groovy:

```groovy
repositories {
    maven { url 'https://jitpack.io' }
    mavenCentral()
}
```

### 2. Add the dependency

```kotlin
dependencies {
    testImplementation("com.github.pdbuczkowski:Given-Perform-Expect:0.0.1")
}
```

Make sure to check for the latest version here:  
👉 [https://jitpack.io/#pdbuczkowski/Given-Perform-Expect](https://jitpack.io/#pdbuczkowski/Given-Perform-Expect)

---

## ⚠️ Note

Version `1.0.0` was published by mistake and should be considered deprecated.  
Please use `0.0.1` or newer.

---

## 🎯 Motivation

Unit tests often become noisy and repetitive. This library provides a simple DSL-like structure to make them more readable and declarative.

Great for:
- Testing pure logic
- Quick prototyping of test cases
- Improving readability and reducing boilerplate

---

## 🔧 Requirements

- Kotlin (JVM)
- Gradle 8+
- Kotlin version 1.8+

---

## 🪪 License

This project is licensed under the MIT License.

See the [LICENSE](LICENSE) file for full details.

---

## 💡 Contributions

Contributions, issues, and feature requests are welcome.  
Feel free to open an issue or a pull request if you find something useful to add or improve.

---

## 🧪 Feedback

If you're using this library and find it useful, feel free to ⭐️ the repo or share your feedback. Thanks!
