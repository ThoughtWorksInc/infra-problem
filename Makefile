BUILD_DIR=build
APPS=front-end quotes
APP_JARS=$(addprefix $(BUILD_DIR)/, $(addsuffix .jar, $(APPS)))

all: $(BUILD_DIR) $(APP_JARS)

test: $(addsuffix .test, $(APPS))

%.test:
	cd $* && lein midje

clean:
	rm -rf $(BUILD_DIR) $(addsuffix /target, $(APPS))

$(APP_JARS):
	cd $(notdir $(@:.jar=)) && lein uberjar && cp target/uberjar/*-standalone.jar ../$@

$(BUILD_DIR):
	mkdir -p $(BUILD_DIR)
