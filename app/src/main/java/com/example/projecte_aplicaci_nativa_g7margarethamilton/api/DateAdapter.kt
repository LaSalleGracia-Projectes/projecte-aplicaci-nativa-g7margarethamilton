import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class ZonedDateTimeAdapter : TypeAdapter<ZonedDateTime>() {
    @RequiresApi(Build.VERSION_CODES.O)
    private val formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME

    @RequiresApi(Build.VERSION_CODES.O)
    override fun write(out: JsonWriter, value: ZonedDateTime?) {
        if (value == null) {
            out.nullValue()
        } else {
            out.value(formatter.format(value))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun read(input: JsonReader): ZonedDateTime? {
        return if (input.peek() == JsonToken.NULL) {
            input.nextNull()
            null
        } else {
            ZonedDateTime.parse(input.nextString(), formatter)
        }
    }
}