#!/bin/bash
jar_dir="$PROJECT_DIR/client-cli/build/compose/jars"
filename_prefix="client-cli-macos-arm64-"
for src in "$jar_dir/$filename_prefix"*.jar; do
    remaining=${src##*client-cli-macos-arm64-}
    remaining=${remaining%.jar}
    echo '#!java -jar' > "$jar_dir/ttt-$remaining"
    cat "$src" >> "$jar_dir/ttt-$remaining"
done
echo "Created executable $jar_dir/ttt-$remaining"