$files = Get-ChildItem -Path "app\src\main\java\com\genpause\app" -Filter "*.kt" -Recurse
foreach ($f in $files) {
    $content = [System.IO.File]::ReadAllText($f.FullName)
    $newContent = $content.Replace("com.zenpause.app", "com.genpause.app")
    [System.IO.File]::WriteAllText($f.FullName, $newContent)
    Write-Host "Updated: $($f.Name)"
}
Write-Host "Done! Updated $($files.Count) files."
