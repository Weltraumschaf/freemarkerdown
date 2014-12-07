# Architecture

The basic  concept is:  You have  templates and  you can  put them  inside other
templates as cascade.

![Overview](images/templates.png)

The "template" is  called `TemplateModel` in the API. Why  this strange name you
may wonder.  The reason is  that I don't wanted  to interfere with  the template
class name  from FreeMarker. That  was the best name  I were possible  to figure
out: It "models what a template is".

There are two  sub types of the  `TemplateModel` in the API:  The `Fragment` and
the  `Layout`.   The  distinction  is  that   you  can  assign  as   many  other
`TemplateModels`  to  it  in contrast  to  a  `Fragment`.  so  if you  assign  a
`Fragment`  to a  `Laout` its  rendered content  is available  in the  `Layout`s
scope as variable:

    final FreeMarkerDown fmd = FreeMarkerDown.create("utf-8");
    ...
    final Fragment inner = fmd.createFragemnt(
        "inner content",
        "utf-8",
        "inner"
    );
    ...
    final Layout outer = fmd.createLayout(
        "outer content. Inner content ${inner}",
        "utf-8",
        "outer"
    );
    outer.assignTemplateModel("inner", inner);
    ...

## API Overview

The public provided API is showed in the following class diagram:

![Overview](uml/overview.svg)

The  main entry  point for  the  API is  the `FreeMarkerDown`  class. All  other
public API  is provided  by interfaces. The  `FreeMarkerDown` class  itself does
not provide  public constrcutors. But it  provides a factory method  to obtain a
fresh  instance. You  should reuse  this object  to create  template models  and
render them. This object handles the  preprocessors and interceptors you want to
use if you have registered them.

## Preprocessors

Preprocessors is a concept from XML.  You can add preprocessor instructions like
`<?targetname  SOME_CONTENT ?>`  to  any  template and  a  preprocesor which  is
registered for that target will  receive the content ("SOME_CONTENT" for example
before) of  the instruction. The instruction  will be removed from  the template
and the return value of the preprocessor  will be insterted at that position. If
you only want to strip the instrcutions, then return an empty string.

So if you implement a pre processor like this:

    class MyPreProcessor implements PreProcessor {

        @Override
        public String process(String input) {
            return ">>" + input + "<<";
        }

        @Override
        public String getTarget() {
            return "myproc";
        }

        @Override
        public boolean hasWarnings() {
            return false;
        }

        @Override
        public Collection<String> getWarnings() {
            return Collections.emptyCollection();
        }
    }

And applies it to a model like this:

    final FreeMarkerDown fmd = FreeMarkerDown.create("utf-8");
    final Fragment inner = fmd.createFragemnt(
          "<?myproc  SOME_CONTENT_1 ?>\n"
        + "inner content\n"
        + "<?myproc  SOME_CONTENT_2 ?>\n",
        "utf-8",
        "inner"
    );
    final Layout outer = fmd.createLayout(
        "outer content.\n"
        + "<?myproc  SOME_CONTENT_3 ?>\n"
        +"${inner}\n");
        + "<?myproc  SOME_CONTENT_4 ?>\n",
        "utf-8",
        "outer"
    outer.assignTemplateModel("inner", inner);
    ...
    final PreProcessor processor = new MyPreProcessor();
    fmd.register(processor);
    fmd.render(outer);

The preprocesor will be called like this:

![Preprocessor Sequence](uml/preprocessor_sequence.svg)

As you  can see the  preprocessor is invoked for  the instructions of  the outer
most template model as they occure  in the template. After that the preprocessor
is invoked for all assigned template modles recursively in no particula order.

## Interceptors

The interceptor concept provides an API  to intercept all stages of the template
rendering process. At the moment there are six execution points:

- `ExecutionPoint#BEFORE_PREPROCESSING`: Interceptors registered for this execution
  point will be invoked _before_ any preprocessor for each template. At this point
  the raw template string is passed to the interceptor as content.
- `ExecutionPoint#AFTER_PREPROCESSING`: Interceptors registered for this execution
  point will be invoked _after_ any preprocessor for each template. At this point
  the preprocessed template string is passed to the interceptor as content.
- `ExecutionPoint#BEFORE_RENDERING`: Interceptors registered for this execution
  point will be invoked _before_ the template will be processed by FreeMarker.
  At this point the preprocessed template string is passed to the interceptor as
  content.
- `ExecutionPoint#AFTER_RENDERING`: Interceptors registered for this execution
  point will be invoked _after_ the template will be processed by FreeMarker.
  At this point the rendered template string is passed to the interceptor as
  content.
- `ExecutionPoint#BEFORE_MARKDOWN`: Interceptors registered for this execution
  point will be invoked _before_ the rendered template string will be converted
  to Markdown. At this point the rendered template string is passed to the
  interceptor as content.
- `ExecutionPoint#AFTER_MARKDOWN`: Interceptors registered for this execution
  point will be invoked _after_ the rendered template string was converted to
  Markdown. At this point the converted string is passed to the interceptor as
  content.

Internal this  is abstracted  as an  event model  in which  each `TemplateModel`
triggers events.  If a template is  given to `FreeMarkerDown` for  rendereing an
event dispatcher  is registered on  that template  model and all  containing sub
template  models.  Any triggered  event  wil  be  forwared to  the  interceptors
registered  for the  particualr execution  point. After  the whole  rendering is
finished `FreeMarkerDown` unregisters the event dispatcher.

![Event Model](uml/event_model.svg)