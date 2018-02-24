# it's time I wrote a language

I've never written one and I'm getting annoyed by Clojure's lack of types.


## Typed Lisp

People first started playing around with types in Lisp.

There have been a couple of decent tries. But all seem to have been academic.

The world of types generally is I guess.

[This](http://cs.brown.edu/~sk/Publications/Books/ProgLangs/) is an
excellent introduction to types using Lisp. But the syntax is made
non-lisp by the addition of types.

[Many](http://docs.racket-lang.org/ts-guide/beginning.html) typed Lisp
[experiments](https://github.com/hraberg/Shen.java) are wordy.

But inspired by [modern](https://crystal-lang.org/) attempts to make
types usable for ordinary programmers I figure I could have a go with
Lisp.


## Sketches of what I'm thinking

Lisp is all about the lambda. Add types to lambda and you're done.

Argument types look easy:

```
  (lambda ((HTTP http)
           (SearchForm form))
    "hello")
```

Just cons cells. You could even go the whole hog with something like:

```
  (lambda ((HTTP . http)
           (SearchForm . form))
    "hello")
```

which (in Scheme) parlance suggests the CDR is not a CONS. This means
that it could be extended later for other argument things; default
values perhaps.

Then again default values are typey:

```
  (lambda ((HTTP http)
           ("q=nic" search-term))
    "hello")
```

so `search-term` is a String because we say it has a default value.


What about return types? Well it's clear the above lambda is a String,
so we shouldn't need to declare it.

But if we wanted or needed to how about:

```
(returns String
  (lambda ((HTTP http))
     "yes!"))
```

If you have these things then I believe you've got everything you need. 

Based on minimal Scheme everything can be done, basically, with lambda
and a few other functions (lazy eval `and` for example).


```
(define my-function
  (returns String
    (lambda ((HTTP http)) "hello World")))
```

Of course, more shortcuts could follow.

### What about eval?

There's a big academic argument about eval. I'm not so sure I need it
or want it. Could we just leave it out?
